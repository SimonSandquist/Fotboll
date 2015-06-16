
public class fielddrawMessage extends Message {

	public int Width;
	public int Height;
	
	/**
	 * 
	 * @param Width the value for the width of the field
	 * @param Height the value for the height of the field
	 */
	public fielddrawMessage(int Width, int Height){
		this.Height = Height;
		this.Width = Width;
		
	}
	
}
