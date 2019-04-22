package ir.amv.snippets.my.api;

/**
 * @author Amir
 */
public class MyOSGiServiceImpl implements IMyOSGiService {
    @Override
    public String concat(final Object... os) {
        StringBuilder sb = new StringBuilder();
        if (os != null) {
            for (Object o : os) {
                sb.append(o);
            }
        }
        return sb.toString();
    }
}
