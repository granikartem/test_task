import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args){
        long time = System.currentTimeMillis();
        if(args.length != 1){
            System.err.println("Wrong amount of arguments!");
            System.exit(1);
        }
        BufferedReader reader = null;
        try {
             reader = new BufferedReader(new FileReader(args[0]));
        } catch (FileNotFoundException e) {
            System.err.println("Can't open this file!");
            System.exit(1);
        }
        ArrayList<HashSet<String>> groups = new ArrayList<>();
        HashMap<Integer, Integer> merges = new HashMap<>();
        ArrayList<HashMap<String, Integer>> dictionary = new ArrayList<>();
        try {
            String line = reader.readLine();
            while(line != null){
                String[] tokens = line.split(";");
                int pos = 0;
                int groupd_id = -1;
                boolean flag = false;
                TreeSet<Integer> local_merges = new TreeSet<>();
                for (String s: tokens) {
                    if (s.equals("\"\"")) {
                        if(dictionary.size() <= pos){
                            dictionary.add(new HashMap<String, Integer>());
                        }
                        pos++;
                        continue;
                    }
                    if (!s.matches("\"[^\"]*\"")) {
                        flag = true;
                        break;
                    }
                    if(dictionary.size() > pos){
                        HashMap<String, Integer> dict = dictionary.get(pos);
                        Integer id = dict.get(s);
                        if(id != null){
                            while(merges.containsKey(id)){
                                id = merges.get(id);
                            }
                            local_merges.add(id);
                        }
                    }else{
                        dictionary.add(new HashMap<String, Integer>());
                    }
                    pos++;
                }
                if(flag){
                    line = reader.readLine();
                    continue;
                }
                if(local_merges.size() > 0){
                    groupd_id = local_merges.pollFirst();
                    groups.get(groupd_id).add(line);
                    for (int i: local_merges) {
                        merges.put(i, groupd_id);
                        groups.get(groupd_id).addAll(groups.get(i));
                        groups.set(i, null);
                    }
                } else{
                    groupd_id = groups.size();
                    groups.add(new HashSet<>());
                    groups.get(groupd_id).add(line);
                }
                for (int i = 0; i < tokens.length; i++) {
                    String s = tokens[i];
                    if (s.equals("\"\"")) {
                        continue;
                    }
                    HashMap<String, Integer> dict = dictionary.get(i);
                    dict.put(s, groupd_id);
                }
                line = reader.readLine();
            }
            while(groups.remove(null)) {}
            groups.sort((Comparator) (o1, o2) -> {
                HashSet<String> s1 = (HashSet<String>) o1;
                HashSet<String> s2 = (HashSet<String>) o2;
                return -(s1.size() - s2.size());
            });
            StringBuilder sb = new StringBuilder();
            int i = 1;
            for (HashSet<String> group: groups) {
                int group_size = group.size();
                if(group_size > 1){
                    sb.append("Группа ").append(i).append("\n");
                    for (String s: group) {
                        sb.append(s).append("\n");
                    }
                    i++;
                }
            }
            time = System.currentTimeMillis() - time;
            sb.append("Время работы программы: ").append((time) / 1000).append(".").append((time) % 1000).append(" секунд");
            sb.insert(0, (i - 1) + "\n");
            Writer writer = new FileWriter("out.txt");
            writer.write(sb.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
