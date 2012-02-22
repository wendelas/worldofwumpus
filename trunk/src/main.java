import java.text.NumberFormat;

import Agents.ChickenLittle;
import Agents.Rambo;
import Agents.pass_fail_test;
import environment.GameBoard;


@SuppressWarnings("unused")
public class main {

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		double pass = 0;
		double fail = 0;
		double give = 0;
		double moves_P = 0;
		double moves_F = 0;
		double moves_G = 0;
		pass_fail_test A = new pass_fail_test();
		NumberFormat nf1 = NumberFormat.getInstance();
		int runs = 0;
		double score_P = 0;
		double score_F = 0;
		double score_G = 0;
		int pit_rate = 0;
		
		pit_rate = 20;
		int size = 4; 
		runs = 0;
		pass = 0;
		fail = 0;
		give = 0;
		score_P = 0;
		score_F = 0;
		score_G = 0;
		moves_P = 0;
		moves_F = 0;
		moves_G = 0;
		//Fear agent
		System.out.println("----------The Agents from F.E.A.R.----------");
		for(pit_rate = 20; pit_rate < 45; pit_rate += 10)
		{
			for(size = 4; size < 13; size+=4)
			{
				runs = 0;
				pass = 0;
				fail = 0;
				give = 0;
				score_P = 0;
				score_F = 0;
				score_G = 0;
				moves_P = 0;
				moves_F = 0;
				moves_G = 0;
				for(int i = 0; i < 666; i++)
				{
					
					GameBoard the_Land = new GameBoard(size, pit_rate); 
					//the_Land.revealed();
					
					ChickenLittle agent = new ChickenLittle(the_Land);
					//agent.what_are_you_thinking();
					A = agent.move(the_Land);
					
					if (A.give_up == true)
					{
						give++;
						score_G += A.score;
						moves_G += A.moves;
					}
					
					else if(A.pass == true)
					{pass++;
						moves_P += A.moves;
						score_P += A.score;} 
					else {
						fail++;
						moves_F += A.moves;
						score_F += A.score;
					}
					runs++;
				}
				System.out.println("World Size: " + size + "\t" + "% of tiles with pits: " + pit_rate);
				System.out.println("Agents:");
				System.out.println("__________________________________________________________________________");
				System.out.println("P/F" + "\t" + "|  " + "Runs:" + "  | " + "# Complete" + "\t" + "|   " + "Avg # steps " + "|" + "\t" + "Avg score"  );
				System.out.println("P Fear: " + "|  " + runs + "\t" + "  |  "+ pass + "\t" + "|   " + nf1.format(moves_P/pass) + "\t" + "|   "  + nf1.format(score_P/pass));
				System.out.println("F Fear: " + "|  " + runs + "\t" + "  |  "+ fail + "\t" + "|   " + nf1.format(moves_F/fail) + "\t" + "|   "  + nf1.format(score_F/fail));
				System.out.println("G Fear: " + "|  " + runs + "\t" + "  |  "+ give + "\t" + "|   " + nf1.format(moves_G/give) + "\t" + "|   "  + nf1.format(score_G/give));
				System.out.println("Fear:   " + "|  " + runs + "\t" + "  |  "+ runs + "\t" + "|   " + nf1.format((moves_F+moves_P+moves_G)/runs) + "\t" + "|   "  + nf1.format((score_P+score_F+score_G)/runs));
				System.out.println("Fear Pass/Fail Ratio: " + nf1.format(pass/(fail+give)));
				System.out.println();
			}
			System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			System.out.println();
		}
		System.out.println();
		System.out.println("--------------------------------------------------------------------------");
		System.out.println("----------Fearless----------");
		//Shadowrunner
		for(pit_rate = 20; pit_rate < 45; pit_rate += 10)
		{
			for(size = 4; size < 13; size+=4)
			{
				runs = 0;
				pass = 0;
				fail = 0;
				give = 0;
				score_P = 0;
				score_F = 0;
				score_G = 0;
				moves_P = 0;
				moves_F = 0;
				moves_G = 0;
				for(int i = 0; i < 666; i++)
				{
					
					GameBoard the_Land = new GameBoard(size, pit_rate); 
					//the_Land.revealed();
					
					Rambo agent = new Rambo(the_Land);
					//agent.what_are_you_thinking();
					A = agent.move(the_Land);
					
					if(A.pass == true)
						{pass++;
						moves_P += A.moves;
						score_P += A.score;}
					else {
						fail++;
						moves_F += A.moves;
						score_F += A.score;
					}
					runs++;
				}
				System.out.println("World Size: " + size + "\t" + "% of tiles with pits: " + pit_rate);
				System.out.println("Agents:");
				System.out.println("__________________________________________________________________________");
				System.out.println("P/F" + "\t" + "|  " + "Runs:" + "  | " + "# Complete" + "\t" + "|   " + "Avg # steps " + "|" + "\t" + "Avg score"  );
				System.out.println("P Fr-1: " + "|  " + runs + "\t" + "  |  "+ pass + "\t" + "|   " + nf1.format(moves_P/pass) + "\t" + "|   "   + nf1.format(score_P/pass));
				System.out.println("F Fr-1: " + "|  " + runs + "\t" + "  |  "+ fail + "\t" + "|   " + nf1.format(moves_F/fail) + "\t" + "|   "  + nf1.format(score_F/fail));
				System.out.println("Fr-1:   " + "|  " + runs + "\t" + "  |  "+ runs + "\t" + "|   " + nf1.format((moves_F+moves_P)/runs) + "\t" + "|   "  + nf1.format((score_P+score_F)/runs));
				System.out.println("Fr-1 Pass/Fail Ratio: " + nf1.format(pass/(fail)));
				System.out.println();
			}
			System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			System.out.println();
		}
	}
}

