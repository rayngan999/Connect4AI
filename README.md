
The Connect Four program has several different command-line switches that you can use to control how the game is played. By default, the two players are human-controlled. You can choose which AI modules to use by using the -p1 and -p2 switches to select the AIModules to use as the first and second player. For example, to pit the RandomAI player against the MonteCarloAI player, you could use:

`java Main -p1 RandomAI -p2 MonteCarloAI`

Any unspecified players will be filled in with human players.
