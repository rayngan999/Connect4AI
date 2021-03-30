// (c) Ian Davidson, Leo Shamis U.C. Davis 2019

import java.util.*;

/// Sample AI module that picks random moves at each point.
/**
 * This AI performs random actions. It is meant to illustrate the basics of how
 * to use the AIModule and GameStateModule classes.
 *
 * Since this terminates in under a millisecond, there is no reason to check for
 * the terminate flag. However, your AI needs to check for the terminate flag.
 *
 * @author Leonid Shamis
 */

class EvalValue {
    public double value;
    public int action;
    public int cur_player;

}

class evalComparator implements Comparator<EvalValue> {

    @Override
    public int compare(EvalValue o1, EvalValue o2) {

        if (o1.cur_player == 1) {
            if (o1.value < o2.value) {
                return 1;
            } else if (o1.value > o2.value) {
                return -1;
            }
        } else {
            if (o1.value > o2.value) {
                return 1;
            } else if (o1.value < o2.value) {
                return -1;
            }
        }
        return 0;
    }

}

public class MyAI_alphabeta extends AIModule {

    public void getNextMove(final GameStateModule game) {

        int action_coloumn = -1;
        double cur = Double.NEGATIVE_INFINITY;
        int level = 0;
        int cutoff = 7;

        double alpha = Double.NEGATIVE_INFINITY;
        double beta = Double.POSITIVE_INFINITY;

        PriorityQueue<EvalValue> best_moves_first = sucessor_function(game, game.getActivePlayer());
        for (EvalValue moves : best_moves_first) {
            if (game.canMakeMove(moves.action)) {
                game.makeMove(moves.action);
                double v = min_value(game, level + 1, cutoff, alpha, beta);
                game.unMakeMove();
                // get Max of (cur, v)
                action_coloumn = getAction(cur, v, action_coloumn, moves.action);
                if (action_coloumn == moves.action) {
                    cur = v;
                }
                if (cur >= beta) {
                    alpha = Math.max(alpha, cur);
                }
            }

        }
        chosenMove = action_coloumn;
    }

    int getAction(double cur, double v, int cur_action, int action) {
        // get max action
        if (v > cur) {
            return action;
        }
        return cur_action;
    }

    PriorityQueue<EvalValue> sucessor_function(GameStateModule game, int cur_player) {
        PriorityQueue<EvalValue> moves = new PriorityQueue<EvalValue>(new evalComparator());
        for (int move = 0; move < game.getWidth(); move++) {
            if (game.canMakeMove(move)) {
                game.makeMove(move);
                EvalValue e = new EvalValue();
                e.action = move;
                e.cur_player = cur_player;
                e.value = eval(game, cur_player);
                moves.add(e);
                game.unMakeMove();
            }
        }

        return moves;
    }

    double max_value(GameStateModule game, int level, int cutoff, double alpha, double beta) {
        if (level == cutoff ) {
            return eval(game, game.getActivePlayer());
        }
        double v = Double.NEGATIVE_INFINITY;

        PriorityQueue<EvalValue> best_moves_first = sucessor_function(game, game.getActivePlayer());
        for (EvalValue moves : best_moves_first) {
            if (game.canMakeMove(moves.action)) {
                game.makeMove(moves.action);
                v = Math.max(v, min_value(game, level + 1, cutoff, alpha, beta));
                game.unMakeMove();
                if (v >= beta) {
                    return v;
                }
                alpha = Math.max(alpha, v);
            }
        }
        return v;
    }

    double min_value(GameStateModule game, int level, int cutoff, double alpha, double beta) {
        if (level == cutoff ) {
            return eval(game, game.getActivePlayer());
        }
        double v = Double.POSITIVE_INFINITY;
        PriorityQueue<EvalValue> best_moves_first = sucessor_function(game, game.getActivePlayer());
        for (EvalValue moves : best_moves_first) {
            if (game.canMakeMove(moves.action)) {
                game.makeMove(moves.action);
                v = Math.min(v, max_value(game, level + 1, cutoff, alpha, beta));
                game.unMakeMove();
                if (v <= alpha) {
                    return v;
                }
                beta = Math.min(beta, v);
            }
        }
        return v;
    }

    double eval(GameStateModule game, int cur_player) {
        double eval_1 = 0.00;
        double eval_2 = 0.00;
        double two_in_a_row = 5;
        double three_in_a_row = 15;

        double winner = 1000;
        // check vertical, horizontal, diagonal
        // only counts the one where it has a chance to win

        for (int col = 0; col < game.getWidth(); col++) {
            for (int row = 0; row < game.getHeight(); row++) {
                int player = game.getAt(col, row);
                if (player == 1) {
                    if ((Math.abs((double) (game.getWidth() - 1) / 2 - col)) == 0) {
                        eval_1 = eval_1 + 30 - (row * 5);
                    } else if ((Math.abs((double) (game.getWidth() - 1) / 2 - col)) == 1) {
                        eval_1 = eval_1 + 15 - (row * 5);
                    } else if ((Math.abs((double) (game.getWidth() - 1) / 2 - col)) == 2) {
                        eval_1 = eval_1 + 5 - (row * 5);
                    } else {
                        eval_1 = eval_1 + 2 - (row * 5);
                    }
                    if (game.getHeight() - row >= 4 && game.getWidth() - col >= 4) {
                        if (game.getAt(col + 1, row + 1) == player && game.getAt(col + 2, row + 2) == player
                                && game.getAt(col + 3, row + 3) == player) {
                            eval_1 = winner;
                        } else if (game.getAt(col + 1, row + 1) == player && game.getAt(col + 2, row + 2) == player
                                && game.getAt(col + 3, row + 3) == 0
                                || game.getAt(col + 2, row + 2) == player && game.getAt(col + 3, row + 3) == player
                                        && game.getAt(col + 1, row + 1) == 0
                                || game.getAt(col + 1, row + 1) == player && game.getAt(col + 3, row + 3) == player
                                        && game.getAt(col + 2, row + 2) == 0) {

                            eval_1 = eval_1 + three_in_a_row;

                        } else if (game.getAt(col + 1, row + 1) == player && game.getAt(col + 2, row + 2) == 0
                                && game.getAt(col + 3, row + 3) == 0) {

                            eval_1 = eval_1 + two_in_a_row;
                        }

                    }
                    if (row >= 4 && game.getWidth() - col >= 4) {
                        if (game.getAt(col + 1, row - 1) == player && game.getAt(col + 2, row - 2) == player
                                && game.getAt(col + 3, row - 3) == player) {
                            eval_1 = winner;
                        }

                        else if (game.getAt(col + 1, row - 1) == player && game.getAt(col + 2, row - 2) == player
                                && game.getAt(col + 3, row - 3) == 0
                                || game.getAt(col + 2, row - 2) == player && game.getAt(col + 3, row - 3) == player
                                        && game.getAt(col + 1, row - 1) == 0
                                || game.getAt(col + 1, row - 1) == player && game.getAt(col + 3, row - 3) == player
                                        && game.getAt(col + 2, row - 2) == 0) {
                            eval_1 = eval_1 + three_in_a_row;
                        }

                        else if (game.getAt(col + 1, row - 1) == player && game.getAt(col + 2, row - 2) == 0
                                && game.getAt(col + 3, row - 3) == 0) {

                            eval_1 = eval_1 + two_in_a_row;
                        }

                    }
                    if (game.getWidth() - col >= 4) {

                        if (game.getAt(col + 1, row) == player && game.getAt(col + 2, row) == player
                                && game.getAt(col + 3, row) == player) {
                            eval_1 = winner;
                        }

                        else if (game.getAt(col + 1, row) == player && game.getAt(col + 2, row) == player
                                && game.getAt(col + 3, row) == 0
                                || game.getAt(col + 2, row) == player && game.getAt(col + 3, row) == player
                                        && game.getAt(col + 1, row) == 0
                                || game.getAt(col + 1, row) == player && game.getAt(col + 3, row) == player
                                        && game.getAt(col + 2, row) == 0) {
                            eval_1 = eval_1 + three_in_a_row;
                        } else if (game.getAt(col + 1, row) == player && game.getAt(col + 2, row) == 0
                                && game.getAt(col + 3, row) == 0) {

                            eval_1 = eval_1 + two_in_a_row;
                        }

                    }
                    if (game.getHeight() - row >= 4) {
                        if (game.getAt(col, row + 1) == player && game.getAt(col, row + 2) == player
                                && game.getAt(col, row + 3) == player) {

                            eval_1 = winner;

                        }

                        else if (game.getAt(col, row + 1) == player && game.getAt(col, row + 2) == player
                                && game.getAt(col, row + 3) == 0) {

                            eval_1 += 0.8 * three_in_a_row;

                        } else if (game.getAt(col, row + 1) == player && game.getAt(col, row + 2) == 0
                                && game.getAt(col, row + 3) == 0) {

                            eval_1 += 0.8 * two_in_a_row;

                        }
                    }
                }
                if (player == 2) {
                    if ((Math.abs((double) (game.getWidth() - 1) / 2 - col)) == 0) {
                        eval_2 = eval_2 + 30 - (row * 5);
                    } else if ((Math.abs((double) (game.getWidth() - 1) / 2 - col)) == 1) {
                        eval_2 = eval_2 + 10 - (row * 5);
                    } else if ((Math.abs((double) (game.getWidth() - 1) / 2 - col)) == 2) {
                        eval_2 = eval_2 + 3 - (row * 5);
                    } else {
                        eval_2 = eval_2 + 1 - (row * 5);
                    }
                    if (game.getHeight() - row >= 4 && game.getWidth() - col >= 4) {
                        if (game.getAt(col + 1, row + 1) == player && game.getAt(col + 2, row + 2) == player
                                && game.getAt(col + 3, row + 3) == player) {
                            eval_2 = winner;
                        }

                        else if (game.getAt(col + 1, row + 1) == player && game.getAt(col + 2, row + 2) == player
                                && game.getAt(col + 3, row + 3) == 0
                                || game.getAt(col + 2, row + 2) == player && game.getAt(col + 3, row + 3) == player
                                        && game.getAt(col + 1, row + 1) == 0
                                || game.getAt(col + 1, row + 1) == player && game.getAt(col + 3, row + 3) == player
                                        && game.getAt(col + 2, row + 2) == 0) {

                            eval_2 = eval_2 + three_in_a_row;

                        } else if (game.getAt(col + 1, row + 1) == player && game.getAt(col + 2, row + 2) == 0
                                && game.getAt(col + 3, row + 3) == 0) {

                            eval_2 = eval_2 + two_in_a_row;
                        }

                    }
                    if (row >= 4 && game.getWidth() - col >= 4) {
                        if (game.getAt(col + 1, row - 1) == player && game.getAt(col + 2, row - 2) == player
                                && game.getAt(col + 3, row - 3) == player) {
                            eval_2 = winner;
                        }

                        else if (game.getAt(col + 1, row - 1) == player && game.getAt(col + 2, row - 2) == player
                                && game.getAt(col + 3, row - 3) == 0
                                || game.getAt(col + 2, row - 2) == player && game.getAt(col + 3, row - 3) == player
                                        && game.getAt(col + 1, row - 1) == 0
                                || game.getAt(col + 1, row - 1) == player && game.getAt(col + 3, row - 3) == player
                                        && game.getAt(col + 2, row - 2) == 0) {
                            eval_2 = eval_2 + three_in_a_row;
                        }

                        else if (game.getAt(col + 1, row - 1) == player && game.getAt(col + 2, row - 2) == 0
                                && game.getAt(col + 3, row - 3) == 0) {

                            eval_2 = eval_2 + two_in_a_row;
                        }

                    }
                    if (game.getWidth() - col >= 4) {

                        if (game.getAt(col + 1, row) == player && game.getAt(col + 2, row) == player
                                && game.getAt(col + 3, row) == player) {
                            eval_2 = winner;
                        }

                        else if (game.getAt(col + 1, row) == player && game.getAt(col + 2, row) == player
                                && game.getAt(col + 3, row) == 0
                                || game.getAt(col + 2, row) == player && game.getAt(col + 3, row) == player
                                        && game.getAt(col + 1, row) == 0
                                || game.getAt(col + 1, row) == player && game.getAt(col + 3, row) == player
                                        && game.getAt(col + 2, row) == 0) {
                            eval_2 = eval_2 + three_in_a_row;
                        } else if (game.getAt(col + 1, row) == player && game.getAt(col + 2, row) == 0
                                && game.getAt(col + 3, row) == 0) {

                            eval_2 = eval_2 + two_in_a_row;
                        }

                    }
                    if (game.getHeight() - row >= 4) {
                        if (game.getAt(col, row + 1) == player && game.getAt(col, row + 2) == player
                                && game.getAt(col, row + 3) == player) {

                            eval_2 = winner;

                        }

                        else if (game.getAt(col, row + 1) == player && game.getAt(col, row + 2) == player
                                && game.getAt(col, row + 3) == 0) {

                            eval_2 += 0.8 * three_in_a_row;

                        } else if (game.getAt(col, row + 1) == player && game.getAt(col, row + 2) == 0
                                && game.getAt(col, row + 3) == 0) {

                            eval_2 += 0.8 * two_in_a_row;

                        }
                    }
                }
            }
        }
        if (cur_player == 2) {
            return eval_1 - eval_2;
        }
        return eval_2 - eval_1;
    }

}