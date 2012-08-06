package abs;

import gnu.trove.list.array.TIntArrayList;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;

import test.Debug;
import classifier.Model;
import classifier.SequenceModel;

public class SeqData extends Data {

	String filename = null;

	// public ArrayList<String> dict = new ArrayList<String>();

	public ArrayList<SeqInstance> instances = new ArrayList<SeqInstance>();

	public SeqData(Model m, String filename) {
		this.filename = filename;
		read(m, this.filename, true);// by default, reading as training data
		// which means new features would be added.
	}

	public SeqData() {
	}

	public void read(Model m, String filename, boolean isTrain) {
		// SeqData prev_data = (SeqData)data;
		SequenceModel model = (SequenceModel) m;
		if (isTrain) {
			if (m.featureFactory.all_features  == null)
				m.featureFactory.all_features = new ArrayList<Feature>();
			if (model.all_states == null)
				model.all_states = new ArrayList<Label>();
			 if (model.all_states.size()==0)
	                model.all_states.add( m.labelFactory.getState("[START]"));
		} else {
			// model.all_features = new ArrayList<Feature>(model.all_features);
			// model.all_states = new ArrayList<State>(model.all_states);
		}
		this.filename = filename;
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(filename));
			// String[] lines = File.ReadAllLines(filename);
			boolean start = true;
			SeqInstance inst = null;
			if (!isTrain && model.all_states.size() == 0)
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
						int idx_all_states = model.all_states.indexOf(state);
						if (idx_all_states != -1) {
							inst.states.add(idx_all_states);
						} else {
							if (isTrain) {
								inst.states.add(model.all_states.size());
								model.all_states.add(state);
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
									inst.features.get(f_pos).add(f.id);
//									model.all_features.add(f);
								}
//							}
						}
					}
				} else {
					start = true;
					instances.add(inst);
				}
			}
			if (!start)
				instances.add(inst);
			
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
			Debug.println("SeqData.read()", e.getMessage());
		}
	}

	/*
	 * public void read(String filename) { BufferedReader reader = null; try {
	 * reader = new BufferedReader(new FileReader(filename)); // String[] lines
	 * = File.ReadAllLines(filename); boolean start = true; Instance inst =
	 * null; if (all_states.size()==0) all_states.add(new State("[START]"));
	 * String line = null; while ((line = reader.readLine())!=null) { if (!line
	 * .equals("")) { if (start) { start = false; inst = new Instance(); }
	 * //read each line, parse to instance String[] items = line.split(" "); if
	 * (items.length < 2) throw new
	 * Exception("this line has less than 2 items!!"); else { State state = new
	 * State(items[items.length - 1]); int idx_all_states =
	 * all_states.indexOf(state); if (idx_all_states == -1) {
	 * inst.states.add(all_states.size()); all_states.add(state); } else {
	 * inst.states.add(idx_all_states); } inst.features.add(new
	 * ArrayList<Integer>()); int f_pos = inst.features.size() - 1; for (int j =
	 * 0; j < items.length - 1; j++) { Feature f = new Feature(items[j]); int
	 * idx_all_features = all_features.indexOf(f); if (idx_all_features == -1) {
	 * inst.features.get(f_pos).add(all_features.size()); all_features.add(f); }
	 * else { inst.features.get(f_pos).add(idx_all_features); } } } } else {
	 * start = true; instances.add(inst); } } if (!start) instances.add(inst); }
	 * catch (Exception e) { Debug.println("SeqData.read()", e.getMessage()); }
	 * }
	 */

}


