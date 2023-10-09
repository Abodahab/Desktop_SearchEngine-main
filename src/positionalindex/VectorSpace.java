package positionalindex;


import java.util.*;

public class VectorSpace {
    
     double scale=Math.pow(10,3);
    HashMap<String ,HashMap<Integer,ArrayList<Integer>>> positional=new HashMap<>();
    HashMap<Integer,Integer> value;
    HashMap<Integer,Double> doc_idf;
   
    
  
    double IDF;
    int docs;
       public VectorSpace (HashMap position,int Docs){
           this.docs=Docs;
//           Query=Qin;
            this.positional=position;
          
       }
      public HashMap Df(){
           HashMap<String ,Integer>df=new HashMap<>();
           int c;
  
           for (String term : positional.keySet()){
             c=0;
               
                 for(int i:positional.get(term).keySet()){
                   c++;
                 }
                 df.put(term,c);
           }
          return df;
      }
    public HashMap Idf(){
          HashMap<String ,Integer>df=new HashMap<>();
          HashMap<String ,Double>idf=new HashMap<>();
          df=Df();
          int c;
//       for(int i=0;i<Query.size();i++){
//           if(!positional.containsKey(Query.get(i))){
//                df.put(Query.get(i),0.0);
//           }
//       }
//       
    
        for (String term : df.keySet()){
            
               c=df.get(term);
               
                
                 if(c>0)
                 {
                     idf.put(term, Math.round((Math.log10(docs/(double)c))*scale)/scale);
                 }
                 else if(c==0)
                     idf.put(term,0.0);
                 
                 }

             return idf; 
    }
    public HashMap termfreq() {
        int t;

        HashMap<String, HashMap<Integer, Integer>> Tf = new HashMap();

        HashMap<Integer, Integer> value1;

//           for(int i=0;i<Query.size();i++){
//               value =new HashMap();
//               if(!positional.containsKey(Query.get(i))){
//                  
//                  for(int j=1;j<=docs;j++){
//                         value.put(j,0.0);
//                       
//                   }
//               
//                freq.put(Query.get(i),value);
//               }
//           }
        for (String term : positional.keySet()) {

            value1 = new HashMap();

            for (int f = 1; f <= docs; f++) {
                for (int i : positional.get(term).keySet()) {
                    if (i != f) {

                        value1.put(f, 0);
                    } else if (i == f) {
                        t = positional.get(term).get(i).size();
                        value1.put(i, t);

                        break;
                    }

                }
            }
            Tf.put(term, value1);
        }

        return Tf;
    }
    public HashMap Raw_tf(){
        HashMap<String, HashMap<Integer, Double>> Rtf = new HashMap();
        HashMap<String, HashMap<Integer, Integer>> Tf = new HashMap();
        HashMap<Integer, Double> value;
        Tf = termfreq();
        double t;
        for (String term : Tf.keySet()) {
            value = new HashMap();

            for (int i : Tf.get(term).keySet()) {
                t = Tf.get(term).get(i);

                if (t > 1) {

                    value.put(i, Math.round((1 + Math.log(t)) * scale) / scale);

                } else if (t == 1) {
                    value.put(i, 1.0);
                } else if (t == 0) {
                    value.put(i, 0.0);
                }
            }
            Rtf.put(term, value);
            
        }
        return Rtf;

    }
    public HashMap TF_IDF(){
       HashMap<String,HashMap<Integer,Double>> tf_idf=new HashMap<>();
       HashMap<String ,Double>df1=new HashMap<>();
       HashMap<String,HashMap<Integer,Double>> Rtf= new HashMap();
       Rtf=Raw_tf();
       double idf;
     int tf;
       df1= Idf();
       for (String term : Rtf.keySet()){
          doc_idf =new HashMap<>();
           if(df1.containsKey(term)==Rtf.containsKey(term)){
               idf=df1.get(term);
               
                 for(int i:Rtf.get(term).keySet()){
                   
                     doc_idf.put(i,Math.round((idf*( Rtf.get(term).get(i)))*scale)/scale);
                
                     
                 }
                 
           }
             tf_idf.put(term, doc_idf);  
           }
      
       return tf_idf;
               
       }

    public HashMap TF_IDFQ(ArrayList Qin){
        ArrayList <String> Query=new ArrayList<String>();
        HashMap<String,Double> tfQ = new HashMap<>();
        HashMap<String,Double> idfQ = new HashMap<>();
        HashMap<String,Double> tf_idfQ = new HashMap<>();
        Query=Qin;
     int c;
         for(int i=0;i<Query.size();i++){
               if(positional.containsKey(Query.get(i))){
                   c=0;
                   for(int j=0;j<Query.size();j++){
                       if(Query.get(i).equalsIgnoreCase(Query.get(j)))
                           c++;
                   }
                   if(c>1)
                        tfQ.put(Query.get(i),Math.round((1+Math.log(c))*scale)/scale);
                   else if(c==1)
                        tfQ.put(Query.get(i),1.0);
                   else if(c==0)
                        tfQ.put(Query.get(i),0.0);
               }
               else {
                     c=0;
                 for(int j=0;j<Query.size();j++){
                     if(Query.get(i).equalsIgnoreCase(Query.get(j)))
                        c++;
                   }
                 if(c>1)
                    tfQ.put(Query.get(i),Math.round((1+Math.log(c))*scale)/scale);
                 else if(c==1)
                    tfQ.put(Query.get(i),1.0);
                 else if(c==0)
                    tfQ.put(Query.get(i),0.0);
               }        
        
    }
         idfQ=Idf();
          for(int i=0;i<Query.size();i++){
               if(idfQ.containsKey(Query.get(i)) && tfQ.containsKey(Query.get(i))){
                   for (String term : idfQ.keySet()){
                         for (String termt : tfQ.keySet()){
                     if(Query.get(i).equalsIgnoreCase(term) && Query.get(i).equalsIgnoreCase(termt)){
             
                   tf_idfQ.put(Query.get(i),Math.round((idfQ.get(term)*tfQ.get(termt))*scale)/scale);
               }
                         }
         }
               }
                   else 
                      tf_idfQ.put(Query.get(i),0.0);
               }
//          for (String i :tf_idfQ.keySet()) {
//                 System.out.println("Term : "+ i  +"\t"+tf_idfQ.get(i));
//           }
          return tf_idfQ;
//             
}
  
    public HashMap Similarity(ArrayList Qin){
         HashMap<String,Double> tf_idfQ = new HashMap<>();
         HashMap<String,HashMap<Integer,Double>> tf_idf=new HashMap<>();
         HashMap<Integer, ArrayList<Integer>> Matched = new HashMap();
         HashMap<String,HashMap<Integer,Double>> Prod=new HashMap<>();
         HashMap<String,Double> NormQ = new HashMap<>();
         HashMap<String,HashMap<Integer,Double>>Norm=new HashMap<>();
          ArrayList <String> Query=new ArrayList<String>();
         HashMap<Integer,Double> value;
         HashMap<Integer,Double> value1;
         HashMap<Integer,Double> Len;
         HashMap<Integer,Integer> Similar;
         Query=Qin;
         tf_idfQ=TF_IDFQ(Query);
         tf_idf=TF_IDF();
         double lenQ=0.0,lenT,sim;
      
         for(String term: tf_idfQ.keySet()){
             lenQ+=Math.pow(tf_idfQ.get(term),2);
    }
        lenQ=(Math.round(((Math.sqrt(lenQ))*scale))/scale);
//        System.out.println(" Length of Q : "+lenQ);
          
             Len=new HashMap<>();
           for (int i=1;i<=docs;i++){
               lenT=0;
            for(String term: tf_idf.keySet()){
              for(int j:tf_idf.get(term).keySet()){
                  if(i==j){
                      lenT+=Math.pow(tf_idf.get(term).get(j),2); 
                      break;
                         }
                    }
              Len.put(i,Math.round((Math.sqrt(lenT))*scale)/scale);
            }
           }
           System.out.println("-------------------- Length Of Documents -------------------- ");
            for (int i :Len.keySet()) {
                 System.out.println("D : "+ i  +"\t"+Len.get(i));
           }
        for(String term: tf_idf.keySet()){
              value=new HashMap<>();
              for(int i:tf_idf.get(term).keySet()){
                  for(int j:Len.keySet()){
                      if(i==j)
                  value.put(i,Math.round(((tf_idf.get(term).get(i))/Len.get(j))*scale)/scale);
              }
              }
              Norm.put(term, value);
              }
        System.out.println("-------------------- Norm of docs --------------------");
         for (String i :Norm.keySet()) {
                 System.out.println("Term : "+ i +"\t"+Norm.get(i));
           }
          for(String term: tf_idfQ.keySet()){
              NormQ.put(term,Math.round(((tf_idfQ.get(term))/lenQ)*scale)/scale);
          }
//           System.out.println("Norm of Q");
//         for (String i :NormQ.keySet()) {
//                 System.out.println("Term : "+ i  +"\t"+NormQ.get(i));
//           }
         for(String term: Norm.keySet()){
               
              for(String termQ :NormQ.keySet()){
                   value=new HashMap<>();
               if(term.equalsIgnoreCase(termQ)){
                 for(int i:Norm.get(term).keySet()){
                     value.put(i,Math.round(((Norm.get(term).get(i))*(NormQ.get(termQ)))*scale)/scale);
             
              }
                 Prod.put(termQ, value);
              }
         }
         }
//          System.out.println("prodddd");
//        for (String i :Prod.keySet()) {
//                 System.out.println("Term : "+ i  +"\t"+Prod.get(i));
//           }
        Similar=new HashMap<>();
        Pharse p = new Pharse(positional,Query, docs);
        Matched=p.PhraseQuery();
        for(int i: Matched.keySet()){
            sim=0;
             for(String term: Prod.keySet()){
              for(int j:Prod.get(term).keySet()){
                  if(i==j){
                       sim+=Prod.get(term).get(j); 
                       
                  }
              }
               Similar.put(i,(int)((sim)*100));
            
        }
        }
//        for (int i=1;i<=docs;i++){
//               sim=0;
//            for(String term: Prod.keySet()){
//              for(int j:Prod.get(term).keySet()){
//                  if(i==j){
//                      sim+=Prod.get(term).get(j); 
//                      break;
//                         }
//                    }
//              Similar.put(i,(int)((sim)*100));
//            }
//           }
//           for (int i :Similar.keySet()) {
//                 System.out.println("D : "+ i  +"\t"+Similar.get(i));
//           }
    return Similar;
}
  public void Ranking (ArrayList Qin){  
        HashMap<Integer,Integer> similar=new HashMap<>();
        HashMap<Integer,Integer> Rank1=new HashMap<>();
        ArrayList <String> Query=new ArrayList<String>();
        int [][]Rank =new int [docs][2];
        int [] copy=new int[docs];
        int [] copy1=new int[docs];
        Query=Qin;
        similar=Similarity(Query);
        if(!similar.isEmpty()){
        int y=0;
        for(int i: similar.keySet()){
            copy[y]=similar.get(i);
            y++;
        }
         Arrays.sort(copy);
         y=0;
         for(int i=copy.length-1;i>0;i--){
             copy1[y]=copy[i];
             y++;
         }
   
        for(int i=0;i<copy1.length;i++){
           
           for(int j: similar.keySet()){
             if(copy1[i]==similar.get(j) && !Rank1.containsKey(j) && similar.get(j)!=0){
                 Rank1.put(j,copy1[i]);
                 Rank[i][0]=j;
                 Rank[i][1]=copy1[i];break;
                 
             }
             }
        }
      
        System.out.println("-------------------- Ranking Of Documents --------------------");
        for(int i=0;i<Rank.length;i++){
            if(Rank[i][1]!=0)
            System.out.println("Document : " +Rank[i][0]+ "\t With Similarity : " +Rank[i][1] );
            
        }
        }
        else
            System.out.println("\n No result , Search for another Query");
}
}