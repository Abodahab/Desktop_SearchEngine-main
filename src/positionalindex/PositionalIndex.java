package positionalindex;

import java.io.*;
import java.util.*;

public class PositionalIndex {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {

        int Docs = 10;
        stemmer s=new stemmer();
        
        File[] files = {new File("doc1.txt"), new File("doc2.txt"), new File("doc3.txt"), new File("doc4.txt"), new File("doc5.txt"), new File("doc6.txt"), new File("doc7.txt"), new File("doc8.txt"), new File("doc9.txt"), new File("doc10.txt")};
        String[] stopwords = {"{", "}", ">", "<", "~", "^", ":", ";", "(", ")", "-", "|", "/", "*", "$", "$", "%", "#", "@", "!", "+", ",", ".", ".", ".", "a", "as", "about", "after", "afterwards", "aint", "all","already", "also", "although", "always", "am", "an", "and","any","are", "arent", "as","at","be","because","been","before","beforehand", "behind", "being","below", "beside", "besides","both","but", "by","did","didnt","do", "does", "doesnt", "doing", "dont", "done","during","each", "edu", "eg","either", "else", "elsewhere",  "from","had", "hadnt","has", "hasnt", "have", "havent", "having", "he", "hes", "her", "here", "heres", "hereafter", "hereby", "herein", "hereupon", "hers", "herself", "hi", "him", "himself", "his", "hither","how","if","immediate", "inasmuch", "inc","into", "inward", "is", "isnt", "it", "itd", "itll", "its", "its", "itself", "just", "last", "lately", "later", "latter", "latterly", "least","little","ltd","mainly", "many","maybe", "me","meanwhile", "merely", "might","more", "moreover","much", "must", "my", "myself", "name", "namely", "nd", "near", "nearly","necessary","neither", "never", "nevertheless", "new", "next", "nine", "no", "nobody", "non", "none", "noone", "nor", "normally", "not", "nothing", "novel", "now", "nowhere", "obviously", "of", "off", "often", "oh", "ok", "okay", "old", "on", "once", "one", "ones", "only", "onto", "or", "other", "others", "otherwise", "ought", "our", "ours", "ourselves", "out", "outside", "over", "overall", "own", "particular", "particularly", "per", "perhaps", "placed", "please", "plus", "possible", "presumably", "probably","que", "quite", "qv", "rather", "rd", "re", "really","reasonably","regardless", "regards", "relatively", "respectively", "right","same", "second", "secondly","self", "selves", "sensible","serious","seriously", "seven", "several","she", "since","so","some","somebody","somehow", "someone", "something", "sometime", "sometimes", "somewhat", "somewhere", "soon", "sorry","sub", "such", "sup", "sure", "ts","th","than","that", "thats", "thats", "the", "their", "theirs", "them", "themselves","thence", "there", "theres", "thereafter", "thereby", "therefore", "therein", "theres", "thereupon", "these", "they", "theyd", "theyll", "theyre", "theyve", "this", "thorough", "thoroughly", "those", "though", "three", "through", "throughout", "thru", "thus", "together", "too","toward", "towards","tries", "truly", "try", "trying", "twice", "two", "un", "under", "unfortunately", "unless", "unlikely", "until", "unto", "up", "upon", "us", "use", "used", "useful", "uses", "using", "usually", "value", "various", "very", "via", "viz", "vs","way","we", "wed", "well","weve", "welcome", "well","whatever", "whereas", "whereby", "wherein", "whereupon", "wherever", "whether","yes", "yet", "you", "youd", "youll", "youre", "youve", "your", "yours", "yourself", "yourselves"};
        Set<String> stopWordSet = new HashSet<String>(Arrays.asList(stopwords));
        ArrayList<Integer> indexes;
        HashMap<String, HashMap<Integer, ArrayList<Integer>>> PostionalIndex = new HashMap();
        HashMap<Integer, ArrayList<Integer>> value = new HashMap();
        HashMap<String ,Double>df=new HashMap<>();
        HashMap<String ,Double>idf=new HashMap<>();
        HashMap<String,HashMap<Integer,Integer>> Tf = new HashMap();
        HashMap<String, HashMap<Integer, Double>> Rtf = new HashMap();
        HashMap<String,HashMap<Integer,Double>> tf_idf=new HashMap<>();
        HashMap<Integer, ArrayList<Integer>> Matched = new HashMap();
        ArrayList<String> fQuery = new ArrayList<>();
         ArrayList<String> query = new ArrayList<>();
        // Fetching all the files
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if (file.isFile()) {
                BufferedReader inputStream = null;
                String line;
                try {
                    inputStream = new BufferedReader(new FileReader(file));
                    int j = 0;
                    while ((line = inputStream.readLine()) != null) {
                        StringTokenizer str = new StringTokenizer(line);
                        while (str.hasMoreTokens()) {
                            String token = str.nextToken().toLowerCase();
                         
                            if (stopWordSet.contains(token)); 
                            else {
                              char[] w =new char [token.length()];
                            for (int m=0;m<token.length();m++)
                            {
                            w[m]=token.charAt(m);
                          
                            }
                            s.add(w, w.length);
                            s.stem();
                            token= s.toString();
                                indexes = new ArrayList<>();
                                value = new HashMap();
                                if (!PostionalIndex.containsKey(token)) {
                                    // a new term
                                    indexes.add(j);
                                    value.put(i + 1, indexes);
                                    PostionalIndex.put(token, value);
                                } else {
                                    // an existing term
                                    if (!PostionalIndex.get(token).containsKey(i + 1)) {
                                        indexes.add(j);
                                        PostionalIndex.get(token).put(i + 1, indexes);
                                    } else {
                                        PostionalIndex.get(token).get(i + 1).add(j);
                                    }
                                }
                                j++;
                            }
                        }
                    }
                } catch (IOException e) {
                    System.out.println(e);
                } finally {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                }
            }
        }

        //print positional index
//        for (String i : PostionalIndex.keySet()) {
//            System.out.println("Term : " + i + PostionalIndex.get(i));
//        }
       VectorSpace d=new VectorSpace(PostionalIndex,Docs);
      
        df=d.Df();
        System.out.println("\n-------------------- Document Frequency -------------------- ");
        for (String i : df.keySet()) {
                 System.out.println("Term : "+ i +"\t" + df.get(i));
           }
        idf=d.Idf();
        System.out.println("\n-------------------- Inverse Document Frequency -------------------- ");
        for (String i : idf.keySet()) {
                 System.out.println("Term : "+ i +"\t" + idf.get(i));
           }
        Tf=d.termfreq();
         System.out.println("\n-------------------- Term Frequency -------------------- ");
             for (String i : Tf.keySet()) {
                 System.out.println("Term : "+ i  +Tf.get(i));
           }
        Rtf=d.Raw_tf();
        System.out.println("\n-------------------- Term Frequency weighting -------------------- ");
           for (String i : Rtf.keySet()) {
                 System.out.println("Term : "+ i  +Rtf.get(i));
           }
        tf_idf=d.TF_IDF();
        System.out.println("\n-------------------- Tf*Idf -------------------- ");
            for (String i : tf_idf.keySet()) {
                 System.out.println("Term : "+ i  +tf_idf.get(i));
           }
        Scanner in = new Scanner(System.in);
        System.out.println("\n enter ur Q:  ");
        String Query = in.nextLine();
        String[] InQ = Query.split(" ");
        for (String str : InQ) {
            if (!stopWordSet.contains(str)) {
                fQuery.add(str.toLowerCase());
            }
        }
        char[] q ;
           for (int m = 0; m < fQuery.size(); m++) {
              q =new char [fQuery.get(m).length()];
               for(int i=0;i<fQuery.get(m).length();i++){
                  q[i] = fQuery.get(m).charAt(i);

               }
               s.add(q,fQuery.get(m).length());
               s.stem();
               query.add(s.toString());   
        }
           
        Pharse p = new Pharse(PostionalIndex, query, Docs);
        Matched=p.PhraseQuery();
        for (int v : Matched.keySet()) {
            if (Matched.get(v) == null) {
                System.out.println("No Matched Documents");
            } else {
                System.out.println("Matched Document NO : " + v);
            }
        }
//              d.Similarity(query);
               d.Ranking(query);

    }

}
    
    
    
    

