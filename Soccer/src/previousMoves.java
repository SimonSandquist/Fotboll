import java.util.ArrayList;


public class previousMoves {

	int ballX, ballY, destX, destY;
	
	public previousMoves(int ballX, int ballY, int destX, int destY){
		this.ballX = ballX;
		this.ballY = ballY;
		this.destX = destX;
		this.destY = destY;
	}
	
/**
 * This function is called by the server gameroom when we check if the move that are being made exists already	
 * @param ballX an integear with the value of the balls x value
 * @param ballY an integear with the value of the balls y value
 * @param destX an int with the value of the move that the player makes with value of the x
 * @param destY an int with the value of the move that the player makes with value of the y
 * @return
 */
public boolean previousMoveExists(int ballX, int ballY, int destX, int destY)
{
	if((ballX == this.ballX && ballY == this.ballY) || (ballX == this.destX && ballY == this.destY))
	{
		if((destX == this.ballX && destY == this.ballY) || (destX == this.destX && destY == this.destY))
		{
			return true;
		}
	}
	
	return false;
	
}

	
}
