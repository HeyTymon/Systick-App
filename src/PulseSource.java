import java.awt.event.ActionListener;

public interface PulseSource
{
    final static byte CONTINUOUS_MODE = 0;
    final static byte BURST_MODE = 1;

    void addActionListener(ActionListener pl);
    void removeActionListener(ActionListener pl);

    void startGeneration();
    void setMode(byte mode);
    byte getMode();
    void stopGeneration();
    void setPulseDelay(int ms);
    int getPulseDelay();
    void setPulseCount(int burst);
}
