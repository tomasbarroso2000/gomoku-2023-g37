package com.example.pdm2324i_gomoku_g37.screens.game

import com.example.pdm2324i_gomoku_g37.domain.Player
import com.example.pdm2324i_gomoku_g37.domain.Turn
import com.example.pdm2324i_gomoku_g37.domain.board.Board
import com.example.pdm2324i_gomoku_g37.domain.board.BoardDraw
import com.example.pdm2324i_gomoku_g37.domain.board.BoardWin
import com.example.pdm2324i_gomoku_g37.domain.User
import com.example.pdm2324i_gomoku_g37.domain.board.Cell


data class GameActivity(
    val users: Pair<User, User>, val board: Board, val currentPlayer: Player
) {
    private fun switchTurn() =
        if (currentPlayer.first == users.first) users.second
        else users.first

    fun computeNewGame(cell: Cell): GameActivity {
        val newBoard = this.board.addPiece(cell)

        return if (newBoard.checkWin(cell))
            this.copy(board = BoardWin(newBoard.positions, this.currentPlayer))
        else if (newBoard.checkDraw())
            this.copy(board = BoardDraw(newBoard.positions))
        else this.copy(
            board = newBoard,
            currentPlayer = Player(this.switchTurn(), this.currentPlayer.second.other())
        )
    }
}