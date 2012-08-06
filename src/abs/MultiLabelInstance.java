package abs;

import java.util.ArrayList;

import classifier.Model;

public class MultiLabelInstance extends Instance{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4438701446745939348L;
	public ArrayList<Label> labels = new ArrayList<Label>();
	@Override
	public void setLabel(Label l){
		if (l == null)
			return;
//		String[] ls = str.split(",");
//		for (String l : ls){
		if (!labels.contains(l))
			labels.add(l);
//		}
	}
	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append("LABELS::");
		for (int i = 0; i < labels.size(); i++){
			sb.append(labels.get(i)+",");
		}
		for (int i = 0; i < featureIndex.size(); i++){
			sb.append(" "+/*Feature.all_features.get*/(featureIndex.get(i))/*.name*/+":"+featureValue.get(i));
		}
		return sb.toString();
	}
	@Override
	public String toString(Model m){
		StringBuffer sb = new StringBuffer();
		sb.append("LABELS::");
		for (int i = 0; i < labels.size(); i++){
			sb.append(labels.get(i)+",");
		}
		for (int i = 0; i < featureIndex.size(); i++){
			sb.append(" "+m.featureFactory.all_features.get(featureIndex.get(i)).name+":"+featureValue.get(i));
		}
		return sb.toString();
	}
}
