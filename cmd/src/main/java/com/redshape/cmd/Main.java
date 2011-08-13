package com.redshape.cmd;

import com.redshape.applications.ApplicationException;
import com.redshape.applications.SpringApplication;
import com.redshape.applications.bootstrap.IBootstrapAction;
import com.redshape.cmd.commands.HelpCommand;
import com.redshape.commands.ExecutionException;
import com.redshape.commands.ICommand;
import com.redshape.commands.ICommandsFactory;
import org.apache.log4j.Logger;

/**
 * @author nikelin
 * @package com.redshape.cmd
 */
public final class Main extends SpringApplication {
    private static final Logger log = Logger.getLogger( Main.class );
    private ICommand actualTask;

    public Main( String[] args ) throws ApplicationException {
        super( args);

        this.processCommands();
    }

    protected ICommandsFactory getCommandsFactory() {
        return getContext().getBean( ICommandsFactory.class );
    }

    @Override 
    public void start() throws ApplicationException  {
        if ( this.actualTask == null ) {
            this.actualTask = new HelpCommand();
        }

        if ( !this.actualTask.isValid() ) {
            log.error("Illegal arguments given");
            this.stop();
        }

        for ( IBootstrapAction action : this.actualTask.getBootstrapRequirements() ) {
            this.getBootstrap().addAction( action );
        }

        super.start();

        try {
            this.processTask(this.actualTask);
        } catch ( IllegalArgumentException e ) {
           System.out.println("Insufficiently or illegal arguments given!");
           System.exit(4);
       } catch ( ExecutionException e )  {
           System.out.println( "Command processing exception");
           System.exit(2);
           e.printStackTrace();
       } catch ( Throwable e ) {
           System.out.println("Something goes wrong...");
           System.exit(1);
           e.printStackTrace();
       }

        System.exit(0);
    }

    protected void processCommands() {
           try {
               String module = null;
               ICommand task = null;
               int i = 0;
               for ( String arg : this.getEnvArgs() ) {
                   if ( i++ == 0 ) {
                       continue;
                   }

                   if ( !arg.startsWith("-") ) {
                       if ( module != null) {
                           if ( task != null ) {
                               this.processTask(task);
                               task = null;
                           }

                           task = this.getCommandsFactory().createTask(module, arg);
                       } else {
                           module = arg;
                       }
                   } else if ( arg.startsWith("-") && task != null  ) {
                       String[] propertyParts = arg.split("=");
                       if ( propertyParts.length < 2 ) {
                           continue;
                       }

                       task.setProperty(
                           propertyParts[0].substring(1),
                           propertyParts[1]
                       );
                   }
               }

               if ( task != null ) {
                   this.actualTask = task;
               } else if ( module != null ) {
                   task = this.getCommandsFactory().createTask(null, module);
                   if ( task != null ) {
                       this.actualTask = task;
                   }
               }
           } catch ( InstantiationException e ) {
               System.out.println("Requested task does not supports! Write `help` for advice.");
               System.exit(3);
           } catch ( IllegalArgumentException e ) {
               System.out.println("Insufficiently or illegal arguments given!");
               System.exit(4);
           } catch ( ExecutionException e )  {
               System.out.println( "Command processing exception");
               System.exit(2);
               e.printStackTrace();
           } catch ( Throwable e ) {
               System.out.println("Something goes wrong...");
               System.exit(1);
               e.printStackTrace();
           }
       }

       private void processTask( ICommand task ) throws ExecutionException {
           if ( !task.isValid() ) {
               throw new IllegalArgumentException();
           }

           task.process();
       }

       public static void main( String[] args ) {
           try {
               Main main = new Main( args );
               main.start();
           } catch ( Throwable e ) {
               log.error( "Excecution exception!", e );
           }
       }


}
