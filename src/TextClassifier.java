import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Set;


public class TextClassifier
{
	
	
	
	public static void main(String[] args) 
	{
		//read minimum 5 files for training
		//user supervises them into minimum 3 categories
		//read query document
		//find euclidean distance
		//sort the documents based on distance in ascending order
		//classify the query document for k=5 (using nearest neighbor)
		//use majority rule for k=5
		
		//List<String> categories=new ArrayList<String>();
		HashMap<String, Integer> categories=new HashMap<String, Integer>();
		List<Document> trainingSet=new ArrayList<Document>();
		final int k=5; //this should be odd value as per wiki. result shows values betwn 3-10 gives better result
		String docName;
		String category;
		
		BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
		
		System.out.println("Experimental Text Classifier using nearest neighbor");
		System.out.println("Expecting Minimum 3 Categories and 5 Documents for Training");
		System.out.println("Value of K is set to" + k);
		System.out.println("============================================================");
		System.out.println("Training Mode...");
		try
		{
			//read training set documents
			do
			{
				System.out.println("Enter document name/path"); //filename .txt files only
				docName=br.readLine();
				System.out.println("Category of document ? "); //technology medical etc.
				category=br.readLine();
				trainingSet.add(new Document(docName, category)); //while reading documents i am using stopwords to reduce common words
																  //also tenses should be considered for better result, however for the sake of simplicity
																  //this program considers stopword list only.
				categories.put(category, 1);//integer part is dummy
											//HashMap is used for unique categories  instead of array list 
				
				System.out.println("Read Another Document (Y/N) ?");
				String result=br.readLine();
				
				result=result.toLowerCase();
				if(result.equals("n") || result.equals("no"))
				{
					if(categories.size()<3 || trainingSet.size()<5)
					{
						System.out.println("Minimum 3 Catgegories and minimum 5 documents expected");
						System.out.println("Abort Program (Y/N)?");
						result=br.readLine();
						result=result.toLowerCase();
						if(result.equals("yes") || result.equals("y"))
						{
							return;
						}
					}
					else
					{
						break;
					}
				}
				
			}while(true);
			
			System.out.println("End of Training\n=======================");
			
			//read query document for classification
			System.out.println("Enter the name/path of document for query");
			docName=br.readLine();
			Document queryDoc=new Document(docName, "unknown"); //dummy mark as unknown
			//find euclidean distance of training set
			List<Result> euclideanDistance=EuclideanDistance.calculate(trainingSet, queryDoc);
			
			//sort the result in ascending order
			Collections.sort(euclideanDistance,new DistanceComprator());
			
			//now for k=5 i.e for first 5 docs find majority
			HashMap<String, Integer> majorityMap=new HashMap<String, Integer>();
			
			for(int i=0;i<k;i++)
			{
				int count;
				if(majorityMap.get(euclideanDistance.get(i).category)==null)
				{
					majorityMap.put(euclideanDistance.get(i).category, 1);
				}
				else
				{
					count=majorityMap.get(euclideanDistance.get(i).category);
					count+=1;
					majorityMap.put(euclideanDistance.get(i).category, count);
				}
			}
			
			Set<String> keyset=majorityMap.keySet();
			String queryDocCategory=null;
			int highest=0;
			boolean first=true;
			
			//find highest frequency of categories for k=5
			for(String key:keyset)
			{
				int temp=majorityMap.get(key);
				
				if(first)
				{
					highest=temp;
					first=false;
					queryDocCategory=key;
				}
				else
				{
					if(temp>highest)
					{
						highest=temp;
						queryDocCategory=key;
					}
				}
			}
			//final output
			System.out.println("Given Query Document is Categorized as : " + queryDocCategory);
		}
		catch(IOException e)
		{
			System.out.println(e.getMessage());
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
	}
	
	static class DistanceComprator implements Comparator<Result>
	{
		@Override
		public int compare(Result o1, Result o2) 
		{
			return o1.distance < o2.distance ? -1 : o1.distance == o2.distance ? 0 : 1;
		}
		
	}

}
