package positionalindex;

import java.util.*;

public class Pharse {

    HashMap<String, HashMap<Integer, ArrayList<Integer>>> Position = new HashMap();
    HashMap<Integer, HashMap<Integer, ArrayList<Integer>>> Phrase = new HashMap();
    HashMap<Integer, ArrayList<Integer>> Doc = new HashMap<>();
    HashMap<Integer, ArrayList<Integer>> check = new HashMap<>();
    ArrayList<Integer> positions = new ArrayList<>();
    ArrayList<Integer>emp=new ArrayList<>();
    ArrayList<String> Query = new ArrayList<String>();
    int Docs;

    public Pharse(HashMap positional, ArrayList query, int Docs) {
        Position = positional;
        Query = query;
        this.Docs = Docs;

    }

    public HashMap PhraseQuery() {
        HashMap<Integer, ArrayList<Integer>> D1 = new HashMap();
        HashMap<Integer, ArrayList<Integer>> D2 ;
        HashMap<Integer, ArrayList<Integer>> res =new HashMap<>() ;
        ArrayList<Integer> s;
        for (int i = 0; i < Query.size(); i++) {
            for (String term : Position.keySet()) {
                Doc = new HashMap<>();
                if (term.equalsIgnoreCase(Query.get(i)) && Position.containsKey(Query.get(i) )) {
                    for (int j : Position.get(term).keySet()) {
                        Doc.put(j,Position.get(term).get(j));
                    }
                     Phrase.put(i, Doc);
                }
                 else if (!Position.containsKey(Query.get(i))){
                    for(int j=0;j<Docs;j++){
                      Doc.put(j,emp);
                      }
                       Phrase.put(i, Doc);
            }

                   
                }
                
        }
        
      
        for (int i : Phrase.keySet()) {
            
            for (int j : Phrase.get(i).keySet()) {
                s = new ArrayList<>();
                for (int f : Phrase.get(i).get(j)) {
                    s.add(f);
                }
                D1.put(j, s);
            }
            break;
        }
        
            for (int term : Phrase.keySet()) {
                D2 = new HashMap();
                for (int j : Phrase.get(term).keySet()) {
                      s = new ArrayList<>();
                        
                    for (int f : Phrase.get(term).get(j)) {
                        if(term!=0){
                          s.add(f);
                        }
                    }
                    if (term != 0 ) {

                        D2.put(j, s);
                            
                    } 
                    
                }  
                
                if (term != 0 && D2!=null ) {
                    res = intersect(D1, D2);
                    D1 = new HashMap();
                    D1 = res;
                }
                 
         
            }
          
            
            return D1;

    }

    private HashMap intersect(HashMap D1, HashMap D2) {
        HashMap<Integer, ArrayList<Integer>> Doc1 = new HashMap();
        HashMap<Integer, ArrayList<Integer>> Doc2 = new HashMap();
        HashMap<Integer, ArrayList<Integer>> result = new HashMap();
        ArrayList<Integer> inter;
        Doc1 = D1;
        Doc2 = D2;
        for (int i = 1; i <= Docs; i++) {
            for (int j : Doc1.keySet()) {
                for (int c : Doc2.keySet()) {
                    if (j == c) {
                        
                        inter = new ArrayList<>();
                        for (int f = 0; f < Doc1.get(j).size(); f++) {
                            for (int s = 0; s < Doc2.get(c).size(); s++) {
                                if (((Doc1.get(j).get(f)) + 1) == Doc2.get(c).get(s)) {
                                    inter.add(Doc2.get(c).get(s));
                                }
                                result.put(j, inter);
                            }
                        }

                    }
                }
            }
        }
        return result;
    }
}
