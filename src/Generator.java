import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Generator extends Thread implements PulseSource {
    ActionListener listener;

    boolean isAlive;
    boolean isWorking;

    int pulseDelay;
    int pulseCount;

    byte mode;
    int burst;
    int ticksLeft = 0;

    Generator(int pulseCount,int pulseDelay,byte mode)
    {
        setPulseCount(pulseCount);
        setPulseDelay(pulseDelay);
        setMode(mode);
        resetTicksLeft();
        isWorking = false;
    }

    public void addActionListener(ActionListener l)
    {
        listener= AWTEventMulticaster.add(listener,l);

    }
    public void removeActionListener(ActionListener pl) {
        AWTEventMulticaster.remove(listener,pl);
    }

    public void run()
    {
        isAlive = true;
        while (isAlive)
        {
            if (isWorking)
            {
                if((checkAlive() && mode == CONTINUOUS_MODE) || (checkAlive() && ticksLeft > 0))
                {
                    try {
                        Thread.sleep(pulseDelay);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    if(listener==null)
                    {
                        System.out.println("Tick");

                        if (mode == BURST_MODE)
                        {
                            System.out.println(ticksLeft);
                            decrementTicksLeft();
                        }
                    }
                    else
                    {
                        listener.actionPerformed(new ActionEvent(this,ActionEvent.ACTION_PERFORMED,"Tick"));
                    }
                }
            }
            else
            {
                try
                {
                    Thread.sleep(1);
                } catch (InterruptedException e)
                {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static void main(String[] args)
    {
        Generator gen = new Generator(5,1000,PulseSource.BURST_MODE);
        gen.start();
        gen.addActionListener(e-> {

                System.out.println("Tick");

                if(gen.getMode() == BURST_MODE)
                {
                    System.out.println(gen.ticksLeft);
                    gen.decrementTicksLeft();

                    if(gen.getTicksLeft() == 0)
                    {
                        gen.resetTicksLeft();
                    }
                }
        });

        JFrame frame = new JFrame();
        JButton b=new JButton("Button 1");

        b.addActionListener(e->{
            if(gen.checkWorking())
            {
                gen.stopGeneration();
            }
            else
            {
                gen.startGeneration();
            }
        });

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(b);
        frame.setSize(400,500);
        frame.setVisible(true);

    }

    public void startGeneration()
    {
        isWorking = true;
    }

    public void stopGeneration()
    {
        isWorking = false;
    }

    public boolean checkWorking()
    {
        return isWorking;
    }

    public boolean checkAlive()
    {
        return isAlive;
    }

    public void killThread()
    {
        isAlive = false;
    }

    public void setMode(byte mode)
    {
        this.mode = mode;
    }

    public byte getMode()
    {
        return mode;
    }

    public void setPulseDelay(int ms)
    {
        pulseDelay = ms;
    }

    public int getPulseDelay()
    {
        return pulseDelay;
    }

    public void setPulseCount(int burst)
    {
        pulseCount = burst;
        this.burst = burst;
    }

    public int getPulseCount()
    {
        return pulseCount;
    }

    public int getTicksLeft()
    {
       return ticksLeft;
    }

    public void decrementTicksLeft()
    {
        ticksLeft--;
    }

    public void resetTicksLeft()
    {
        ticksLeft = getPulseCount();
    }

}
