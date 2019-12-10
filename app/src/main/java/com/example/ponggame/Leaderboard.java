package com.example.ponggame;

public class Leaderboard implements Comparable<Leaderboard> {
    private String user;
    private int score;

    public Leaderboard (String user, int score) {
        this.user = user;
        this.score = score;
    }

    public Leaderboard () {

    }

    public int getScore() {
        return score;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public int compareTo(Leaderboard leaderboard) {
        if (score < leaderboard.score) {
            return 1;
        }
        if (score > leaderboard.score) {
            return -1;
        }
        return 0;
    }
}
