package simulator;



/** 
 * Class used for storing a double and an index.
 * Acts a little bit like simulator.Sorter.ComparibleHolder but
 * is more specialised and probably faster and is just simpler
 * and nicer to look at.
 * @author Robby McKilliam
 */
public class IndexedDouble implements Comparable{
    public double value;
    public int index;

    public int compareTo(Object o) {
        IndexedDouble co = (IndexedDouble) o;
        return Double.compare(value, co.value);
    } 
   
}