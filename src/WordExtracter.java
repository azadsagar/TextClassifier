import java.io.FileInputStream;
import java.io.IOException;
//import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
//import java.util.Set;


public class WordExtracter 
{
	private final String fileName;
	
	public WordExtracter(String fileName)
	{
		this.fileName=fileName;
		
	}
	
	public HashMap<String, Integer> startExtracting() throws Exception
	{
		StringBuffer word=new StringBuffer();
		HashMap<String, Integer> wordList=new HashMap<String,Integer>();
		String temp=null;
		FileInputStream in=null;
		//PrintWriter out=null;
		
		boolean wordFound=false;
		int frequency=0;
		
		List<String> stopWordList=new ArrayList<String>();
		FileInputStream stopWords=null;
		
		try
		{
			stopWords=new FileInputStream("stopwords.txt");
			StringBuffer sb=new StringBuffer();
			
			int c;
			char ch;
			
			while((c=stopWords.read())!=-1)
			{
				ch=(char)c;
				sb.append(ch);
			}
			
			String[] tmpStopWords=sb.toString().split("\\r?\\n");
			
			for(String str:tmpStopWords)
			{
				stopWordList.add(str);
			}
			
		}
		catch(IOException e)
		{
			throw new Exception("Stop Word List not found");
		}
		finally
		{
			if(stopWords!=null)
			{
				stopWords.close();
			}
		}
		
		try
		{
			in=new FileInputStream(fileName);
			
			int c;
			char ch;
			
			while((c=in.read())!=-1)
			{
				ch=(char)c;
				
				// word should begin with capital letter or small letter
				if((c>='A' && c<='Z') || (c>='a' && c<='z'))
				{
					if(wordFound)
					{
						//append a new character to string
						word.append(ch);
					}
					else
					{
						//word just found
						wordFound=true;
						word.append(ch);
					}
				}
				else
				{
					if(wordFound)
					{
						//if it is white space tab space new line or carriage return
						//or a full stop this is the end of word
						//add the word to HashMap
						
						
						if(ch==',' || ch==')' || ch=='\"' || ch==';' || ch==':' || ch==' ' || ch=='\t' || ch=='\n' || ch=='\r' || ch=='.')
						{
							//end of the word
							//lower case the word
							temp=word.toString().toLowerCase();
							
							if(wordList.get(temp)!=null)
							{
								//word already exist
								frequency=wordList.get(temp).intValue();
								frequency++;//Increment the counter
								
								wordList.put(temp, frequency);
								
								//delete string buffer
								
								word.setLength(0);
								
							}
							else
							{
								//add a new word to hashmap
								//before adding check if word is stopwordlist word
								if(!stopWordList.contains(temp))
								{
									wordList.put(temp,1);
								}
								
								word.setLength(0);
							}
							
							wordFound=false;
						}
						else
						{
							//allow rest of the character to be appended in string
							//although this doesn't make sense, try rest of the sets and combinations with "if block"
							word.append(ch);
						}
						
						
						
					}//else skip the characters as garbage
					
				}
				
			}
			
			//if loop was broken coz of end of file, then last word should be added to hashmap
			if(wordFound)
			{
				temp=word.toString().toLowerCase();
				
				if(wordList.get(temp)!=null)
				{
					//word already exist
					frequency=wordList.get(temp).intValue();
					frequency++;//Increment the counter
					
					wordList.put(temp, frequency);
					
					//delete string buffer
					
					word.setLength(0);
					
				}
				else
				{
					//add a new word to hashmap
					if(!stopWordList.contains(temp))
					{
						wordList.put(temp,1);
					}
					
					word.setLength(0);
				}
			}
			
			//uncomment to store the frequency map to output file
			/*Set<String> wordSets=wordList.keySet();
			
			out=new PrintWriter(fileName + ".freq");
			
			for(String tword: wordSets)
			{
				out.println(tword + " " + wordList.get(tword).toString());
				//System.out.println(tword + " " + wordList.get(tword).toString());
			}*/
		}
		catch(IOException e)
		{
			throw new Exception(e.getMessage());
		}
		finally
		{
			if(in!=null)
			{
				in.close();
			}
			
			/*if(out!=null)
			{
				out.close();
			}*/
			
		}
		
		return wordList;
	}

}
