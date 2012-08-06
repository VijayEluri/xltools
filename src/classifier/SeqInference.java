package classifier;

import java.util.ArrayList;

import abs.Label;
import abs.SeqInstance;


public abstract class SeqInference extends Inference {

    //given a sequential observation, find the most possible output
    public abstract ArrayList<Label> findBestSeq(SeqInstance x) ;
	
}
