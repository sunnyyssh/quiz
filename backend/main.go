package main

import (
	"context"
	"errors"
	"log"
	"os"
	"strconv"

	"github.com/gin-gonic/gin"
	"github.com/jackc/pgx/v5"
	"github.com/jackc/pgx/v5/pgxpool"
)

func main() {
	ctx := context.Background()

	pgConn := os.Getenv("PG_CONN_STRING")
	if pgConn == "" {
		log.Fatal("Set PG_CONN_STRING env variable")
	}

	db, err := pgxpool.New(ctx, pgConn)
	if err != nil {
		log.Fatalf("failed to connect pg: %s", err)
	}
	defer db.Close()

	q := `CREATE TABLE IF NOT EXISTS quizes (id SERIAL, data JSONB)`
	if _, err := db.Exec(ctx, q); err != nil {
		log.Fatalf("migration failed: %s", err)
	}

	repo := NewQuizRepo(db)

	r := gin.New()

	r.GET("/quiz/:id", getQuiz(repo))
	r.POST("/quiz", createQuiz(repo))
	r.PUT("/quiz/:id/answers", getQuizAnswers(repo))

	if err := r.Run(":8088"); err != nil {
		log.Fatalf("gin running failed: %s", err)
	}
}

func getQuiz(repo *QuizRepo) func(c *gin.Context) {
	return func(c *gin.Context) {
		id, err := strconv.ParseInt(c.Param("id"), 10, 64)
		if err != nil {
			c.String(400, err.Error())
			return
		}

		quiz, err := repo.Get(c.Request.Context(), id)
		if err != nil {
			if errors.Is(err, pgx.ErrNoRows) {
				c.String(404, err.Error())
				return
			}
			c.String(500, err.Error())
		}

		qs := make([]gin.H, 0)
		for _, q := range quiz.Questions {
			qs = append(qs, gin.H{
				"ask":     q.Ask,
				"options": q.Options,
			})
		}

		c.JSON(200, gin.H{
			"id":        quiz.ID,
			"title":     quiz.Title,
			"questions": qs,
		})
	}
}

func createQuiz(repo *QuizRepo) func(c *gin.Context) {
	return func(c *gin.Context) {
		quiz := new(Quiz)
		if err := c.ShouldBindBodyWithJSON(quiz); err != nil {
			c.String(400, "invalid format")
			return
		}

		quiz, err := repo.Create(c.Request.Context(), quiz)
		if err != nil {
			c.String(500, err.Error())
			return
		}

		c.String(200, strconv.FormatInt(quiz.ID, 10))
	}
}

func getQuizAnswers(repo *QuizRepo) func(c *gin.Context) {
	return func(c *gin.Context) {
		id, err := strconv.ParseInt(c.Param("id"), 10, 64)
		if err != nil {
			c.String(400, err.Error())
			return
		}

		quiz, err := repo.Get(c.Request.Context(), id)
		if err != nil {
			if errors.Is(err, pgx.ErrNoRows) {
				c.String(404, err.Error())
				return
			}
			c.String(500, err.Error())
		}

		qs := make([]string, 0)
		for _, q := range quiz.Questions {
			qs = append(qs, q.Answer)
		}

		c.JSON(200, qs)
	}
}
