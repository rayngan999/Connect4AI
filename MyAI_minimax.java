// (c) Ian Davidson, Leo Shamis U.C. Davis 2019

import java.util.*;

/// Sample AI module that picks random moves at each point.
/**
 * This AI performs random actions.  It is meant to illustrate the basics of how to
 * use the AIModule and GameStateModule classes.
 *
 * Since this terminates in under a millisecond, there is no reason to check for
 * the terminate flag.  However, your AI needs to check for the terminate flag.
 *
 * @author Leonid Shamis
 */
public class MyAI_minimax extends AIModule
{
	public void getNextMove(final GameStateModule game)
	{

        int action_coloumn = -1;
        double cur = Double.NEGATIVE_INFINITY;
        int level = 0;
        int cutoff = 6;

        for (int move = 0; move < game.getWidth() ; move++) {
            // int cur_player = game.getActivePlayer();
            if(game.canMakeMove(move)){
                game.makeMove(move);
                double v = min_value(game, level + 1, cutoff);
                game.unMakeMove();
                action_coloumn = getAction(cur, v, action_coloumn, move);
                if (action_coloumn == move) {
                    cur = v;
                }
            }
        }
        chosenMove = action_coloumn;
		
	}

    int getAction (double cur, double v, int cur_action, int action){
        //get max action
        if(v> cur){
            return action;
        }
        return cur_action;
    }

    double max_value(GameStateModule game, int level, int cutoff){
        if(level == cutoff){
            return eval(game);
        }
        double v = Double.NEGATIVE_INFINITY;
        for (int move = 0; move < game.getWidth(); move++) {
            if (game.canMakeMove(move)){
                game.makeMove(move);
                v = Math.max(v , min_value(game , level + 1, cutoff));
                game.unMakeMove();
            }
        }
        return v;
    }

    double min_value(GameStateModule game, int level, int cutoff){
        if(level == cutoff ){
            return eval(game);
        }
        double v = Double.POSITIVE_INFINITY;
        for (int move = 0; move < game.getWidth(); move++) {
            if (game.canMakeMove(move)){
                game.makeMove(move);
                v = Math.min(v , max_value(game , level + 1 , cutoff));
                game.unMakeMove();
            }
        }
        return v;
    }

    double eval(GameStateModule game){
        double eval_1 = 0.00;
        double eval_2 = 0.00;
        double two_in_a_row = 5;
        double three_in_a_row = 15;

        double winner = 1000;
        // check vertical, horizontal, diagonal
        // only counts the one where it has a chance to win

        for (int col = 0; col < game.getWidth(); col++){
            for (int row = 0; row < game.getHeight(); row++){
                int player = game.getAt(col,row);
                if(player == 1){
                    if((Math.abs((double) (game.getWidth()-1)/2 - col)) == 0){
                        eval_1 = eval_1 + 30 -(row*5);
                    }
                    else if ((Math.abs((double) (game.getWidth()-1)/2 - col)) == 1){
                       eval_1 = eval_1 + 15 -(row*5);
                    }
                    else if ((Math.abs((double) (game.getWidth()-1)/2 - col)) == 2){
                        eval_1 = eval_1 + 5 -(row*5);
                     }
                    else{
                       eval_1 = eval_1  + 2  -(row*5);
                    }
                   if(game.getHeight() - row >=  4 && game.getWidth() - col >=  4){
                       if(game.getAt(col+1,row+1) == player && game.getAt(col+2,row+2) == player && game.getAt(col+3,row+3) == player){
                           eval_1 = winner;
                       }
                       else if(game.getAt(col+1,row+1) == player && game.getAt(col+2,row+2) == player && game.getAt(col+3,row+3) == 0 || game.getAt(col+2,row+2) == player && game.getAt(col+3,row+3) == player && game.getAt(col+1,row+1) == 0  || game.getAt(col+1,row+1) == player && game.getAt(col+3,row+3) == player && game.getAt(col+2,row+2) == 0){
                 
                           eval_1 = eval_1 + three_in_a_row;
                           
                       }
                       else if(game.getAt(col+1,row+1) == player && game.getAt(col+2,row+2) == 0 && game.getAt(col+3,row+3) == 0 ){
                          
                           eval_1 = eval_1 + two_in_a_row;
                       }
                       
                       
                       
                   }
                   if(row >= 4 && game.getWidth() - col>=  4){
                       if(game.getAt(col+1,row-1) == player && game.getAt(col+2,row-2) == player && game.getAt(col+3,row-3) == player){
                           eval_1 = winner;
                       }
                    
                       else if(game.getAt(col+1,row-1) == player && game.getAt(col+2,row-2) == player && game.getAt(col+3,row-3) == 0 ||game.getAt(col+2,row-2) == player && game.getAt(col+3,row-3) == player && game.getAt(col+1,row-1) == 0 || game.getAt(col+1,row-1) == player && game.getAt(col+3,row-3) == player && game.getAt(col+2,row-2) == 0){
                           eval_1 = eval_1 + three_in_a_row;
                       }

                       else if(game.getAt(col+1,row-1) == player && game.getAt(col+2,row-2) == 0 && game.getAt(col+3,row-3) == 0 ){
                         
                           eval_1 = eval_1 + two_in_a_row;
                       }
                       
                       
                       
                   }
                   if(game.getWidth() - col >= 4){

                       if(game.getAt(col+1,row) == player && game.getAt(col+2,row) == player && game.getAt(col+3,row) == player){
                           eval_1 = winner;
                       }
                
                       else if(game.getAt(col+1,row) == player && game.getAt(col+2,row) == player && game.getAt(col+3,row) == 0 || game.getAt(col+2,row) == player && game.getAt(col+3,row) == player && game.getAt(col+1,row) ==  0|| game.getAt(col+1,row) == player && game.getAt(col+3,row) == player && game.getAt(col+2,row) ==  0){
                           eval_1 = eval_1 + three_in_a_row;
                       }
                       else if(game.getAt(col+1,row) == player && game.getAt(col+2,row) == 0 && game.getAt(col+3,row) == 0){
                       
                           eval_1 = eval_1 + two_in_a_row;
                       }
                       
                       
                       
                   }
                   if(game.getHeight() - row >= 4){
                       if (game.getAt(col,row+1) == player && game.getAt(col,row+2) == player && game.getAt(col,row+3) == player ){
                             
                           eval_1 = winner;
                  
                           
                       }
                       
                       else if ( game.getAt(col,row+1) == player && game.getAt(col,row+2) == player && game.getAt(col,row+3) == 0){
                   
            
                           eval_1 +=  0.8*three_in_a_row;
                        
                           }
                           else if(game.getAt(col,row+1) == player && game.getAt(col,row+2) == 0 && game.getAt(col,row+3) == 0){
            
                           eval_1 +=  0.8*two_in_a_row;
                               
                           }
                   }
               }
                if(player == 2){
                     if((Math.abs((double) (game.getWidth()-1)/2 - col)) == 0){
                         eval_2 = eval_2 + 30 -(row*5);
                     }
                     else if ((Math.abs((double) (game.getWidth()-1)/2 - col)) == 1){
                        eval_2 = eval_2 + 10 -(row*5);
                     }else if ((Math.abs((double) (game.getWidth()-1)/2 - col)) == 2){
                        eval_2 = eval_2 + 3 -(row*5);
                    }
                     else{
                        eval_2 = eval_2 +1 -(row*5);
                     }
                    if(game.getHeight() - row >=  4 && game.getWidth() - col >=  4){
                        if(game.getAt(col+1,row+1) == player && game.getAt(col+2,row+2) == player && game.getAt(col+3,row+3) == player){
                            eval_2 = winner;
                        }
    
                        else if(game.getAt(col+1,row+1) == player && game.getAt(col+2,row+2) == player && game.getAt(col+3,row+3) == 0 || game.getAt(col+2,row+2) == player && game.getAt(col+3,row+3) == player && game.getAt(col+1,row+1) == 0  || game.getAt(col+1,row+1) == player && game.getAt(col+3,row+3) == player && game.getAt(col+2,row+2) == 0){
                  
                            eval_2 = eval_2 + three_in_a_row;
                            
                        }
                        else if(game.getAt(col+1,row+1) == player && game.getAt(col+2,row+2) == 0 && game.getAt(col+3,row+3) == 0 ){
                           
                            eval_2 = eval_2 + two_in_a_row;
                        }
                        
                        
                        
                    }
                    if(row >= 4 && game.getWidth() - col>=  4){
                        if(game.getAt(col+1,row-1) == player && game.getAt(col+2,row-2) == player && game.getAt(col+3,row-3) == player){
                            eval_2 = winner;
                        }
                      
                        else if(game.getAt(col+1,row-1) == player && game.getAt(col+2,row-2) == player && game.getAt(col+3,row-3) == 0 ||game.getAt(col+2,row-2) == player && game.getAt(col+3,row-3) == player && game.getAt(col+1,row-1) == 0 || game.getAt(col+1,row-1) == player && game.getAt(col+3,row-3) == player && game.getAt(col+2,row-2) == 0){
                            eval_2 = eval_2 + three_in_a_row;
                        }

                        else if(game.getAt(col+1,row-1) == player && game.getAt(col+2,row-2) == 0 && game.getAt(col+3,row-3) == 0 ){
                          
                            eval_2 = eval_2 + two_in_a_row;
                        }
                        
                        
                        
                    }
                    if(game.getWidth() - col >= 4){

                        if(game.getAt(col+1,row) == player && game.getAt(col+2,row) == player && game.getAt(col+3,row) == player){
                            eval_2 = winner;
                        }
                       
                        else if(game.getAt(col+1,row) == player && game.getAt(col+2,row) == player && game.getAt(col+3,row) == 0 || game.getAt(col+2,row) == player && game.getAt(col+3,row) == player && game.getAt(col+1,row) ==  0|| game.getAt(col+1,row) == player && game.getAt(col+3,row) == player && game.getAt(col+2,row) ==  0){
                            eval_2 = eval_2 + three_in_a_row;
                        }
                        else if(game.getAt(col+1,row) == player &&  game.getAt(col+2,row) == 0  &&  game.getAt(col+3,row) == 0){
                        
                            eval_2 = eval_2 + two_in_a_row;
                        }
                        
                        
                        
                    }
                    if(game.getHeight() - row >= 4){
                        if (game.getAt(col,row+1) == player && game.getAt(col,row+2) == player && game.getAt(col,row+3) == player ){
                              
                            eval_2 = winner;
                   
                            
                        }
                        
                        else if ( game.getAt(col,row+1) == player && game.getAt(col,row+2) == player && game.getAt(col,row+3) == 0){
                    
             
                            eval_2 +=  0.8*three_in_a_row;
                         
                            }
                            else if(game.getAt(col,row+1) == player && game.getAt(col,row+2) == 0 && game.getAt(col,row+3) == 0){
             
                            eval_2 +=  0.8*two_in_a_row;
                                
                            }
                    }
                }
            }
        }
        if(game.getActivePlayer() == 1){
            return eval_1 - eval_2;
        }
        return  eval_2 - eval_1 ;
    }

}

