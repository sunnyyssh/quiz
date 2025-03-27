package main

import (
	"context"
	"encoding/json"

	"github.com/jackc/pgx/v5/pgxpool"
)

type Question struct {
	Ask     string   `json:"ask"`
	Options []string `json:"options"`
	Answer  string   `json:"answer"`
}

type Quiz struct {
	ID        int64      `json:"-"`
	Questions []Question `json:"questions"`
}

type QuizRepo struct {
	db *pgxpool.Pool
}

func NewQuizRepo(db *pgxpool.Pool) *QuizRepo { return &QuizRepo{db} }

func (r *QuizRepo) Create(ctx context.Context, quiz *Quiz) (*Quiz, error) {
	jsonQuiz, err := json.Marshal(quiz)
	if err != nil {
		return nil, err
	}

	q := `INSERT INTO quizes (data) VALUES ($1) RETURNING id`

	if err := r.db.QueryRow(ctx, q, jsonQuiz).Scan(&quiz.ID); err != nil {
		return nil, err
	}
	return quiz, nil
}

func (r *QuizRepo) Get(ctx context.Context, id int64) (*Quiz, error) {
	q := `SELECT data FROM quizes WHERE id = $1`

	var jsonQuiz []byte
	if err := r.db.QueryRow(ctx, q, id).Scan(&jsonQuiz); err != nil {
		return nil, err
	}

	var quiz Quiz
	if err := json.Unmarshal(jsonQuiz, &quiz); err != nil {
		return nil, err
	}

	quiz.ID = id
	return &quiz, nil
}
