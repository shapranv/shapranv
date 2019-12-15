import shapranv.ryanair.client.RyanairClientRootDefinition;
import shapranv.shell.utils.application.ApplicationDsl;

public class RyanairClientRunner extends ApplicationDsl {

    public static class UAT {
        public static void main(String[] args) {
            run(args, new ApplicationConfiguration()
                    .rootModule(RyanairClientRootDefinition.class));
        }
    }
}
