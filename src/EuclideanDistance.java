import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class EuclideanDistance
{
	
	public static List<Result> calculate(List<Document> trainingSet,Document queryDoc)
	{
		List<Result> resultList=new ArrayList<Result>();
		double distance;		
		
		for(Document document:trainingSet)
		{
			double dist=0;
			Set<String> keyset=queryDoc.words.keySet();
			
			for(String key:keyset)
			{
				if(document.words.get(key)!=null)
				{
					dist+=Math.pow(document.words.get(key)-queryDoc.words.get(key), 2);
				}
				else
				{
					//words which are in queryDoc but not in training set doc
					dist+=Math.pow(queryDoc.words.get(key),2); 
				}
			}
			
			//and now keyset for all those words which are not in queryDoc
			
			keyset=document.words.keySet();
			for(String key:keyset)
			{
				if(queryDoc.words.get(key)==null)
				{
					dist+=Math.pow(document.words.get(key),2);
				}
			}
			
			distance=Math.sqrt(dist);
			resultList.add(new Result(distance, document.category));
		}
		
		return resultList;
		
	}

}
