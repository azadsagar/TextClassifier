import java.util.HashMap;


public class Document 
{
	public HashMap<String, Integer> words=null;
	public String category=null;
	
	public Document(String fileName,String category) throws Exception
	{
		this.category=category.toLowerCase();
		words=new WordExtracter(fileName).startExtracting();
	}

}
