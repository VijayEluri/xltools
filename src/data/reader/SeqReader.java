package data.reader;

import gnu.trove.list.array.TIntArrayList;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;

import test.Debug;
import abs.Feature;
import abs.Label;
import abs.SeqData;
import abs.SeqInstance;
import classifier.Model;

public class SeqReader {
	/**
	 * sequence data reader
	 */
	public static SeqData read(String filename, Model m, boolean isTrain){
		// SeqData prev_data = (SeqData)data;
		SeqData data = new SeqData();
		Model model = (Model) m;
		if (isTrain) {
			if (m.featureFactory.all_features  == null)
				m.featureFactory.all_features = new ArrayList<Feature>();
			if (m.labelFactory.all_labels == null)
				m.labelFactory.all_labels = new ArrayList<Label>();
		} else {
			// model.all_features = new ArrayList<Feature>(model.all_features);
			// model.all_states = new ArrayList<State>(model.all_states);
		}
		
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(filename));
			// String[] lines = File.ReadAllLines(filename);
			boolean start = true;
			SeqInstance inst = null;
			if (!isTrain && m.labelFactory.all_labels.size() == 0)
				Debug.println("missing model states in test phase!");
			// all_states.add(new State("[START]"));
			String line = null;
			if (isTrain)
				m.featureFactory.clear(); // clear the feature history
			else
				m.featureFactory.isTrain = false;
			while ((line = reader.readLine()) != null) {
				if (!line.equals("")) {
					if (start) {
						start = false;
						inst = new SeqInstance();
					}
					// read each line, parse to instance
					String[] items = line.split(" ");
					if (items.length < 2)
						throw new Exception("this line has less than 2 items!!");
					else {
						Label state = m.labelFactory.getState(items[items.length - 1]);
						int idx_all_states = m.labelFactory.all_labels.indexOf(state);
						if (idx_all_states != -1) {
							inst.states.add(idx_all_states);
						} else {
							if (isTrain) {
								inst.states.add(m.labelFactory.all_labels.size());
								m.labelFactory.all_labels.add(state);
							}
							else
							{
								Debug.println("Invalid state @ SeqData.read()");
							}
						}
						inst.features.add(new TIntArrayList());
						int f_pos = inst.features.size() - 1;
						for (int j = 0; j < items.length - 1; j++) {
							Feature f = m.featureFactory.getFeature(items[j]);
//							int idx_all_features = model.all_features.indexOf(f);
//							if (idx_all_features != -1) {
//								inst.features.get(f_pos).add(idx_all_features);
//							} else {
								if (f!=null) {
									// FIXME
									inst.features.get(f_pos).add(m.featureFactory.all_features.indexOf(f));
//									model.all_features.add(f);
								}
//							}
						}
					}
				} else {
					start = true;
					data.instances.add(inst);
				}
			}
			if (!start)
				data.instances.add(inst);
//			f.freq++;	
			Iterator<Feature> it = m.featureFactory.featureNames.values().iterator();
			Feature[] feas = new Feature[m.featureFactory.featureNames.size()];
			Feature ff = null;
			while (it.hasNext()){
				ff = it.next();
				feas[ff.id] = ff;
			}
			m.featureFactory.all_features.ensureCapacity(feas.length);
			for (int i = 0; i < feas.length; i++){
				m.featureFactory.all_features.add(feas[i]);
			}
			
		} catch (Exception e) {
			Debug.println("ClassData.read()", e.getMessage());
		}
		return data;
	}

}
