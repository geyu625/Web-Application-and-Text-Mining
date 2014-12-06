import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GroupMethod {
	public List<String> regularGroup(String pattern, String matcher) {
		Pattern p = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(matcher);
        List<String> list = new ArrayList<String>();
        while (m.find()) {
        	list.add(m.group(1));
        }
        return list;
    }
}
