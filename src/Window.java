import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Window implements ActionListener, ChangeListener
{
    private JFrame frame = new JFrame();
    private JPanel mainPanel;
    private JPanel valuePanel;
    private JPanel progressPanel;
    private JPanel generatorPanel;
    private JCheckBox enableCheckBox;
    private JCheckBox internalCheckBox;
    private JCheckBox externalCheckBox;
    private JCheckBox interruptCheckBox;
    private JTextArea testTextArea;
    private JLabel rvrLabel;
    private JLabel rvrValueLabel;
    private JLabel cvrLabel;
    private JLabel cvrValueLabel;
    private JLabel enableLabel;
    private JLabel internalLabel;
    private JLabel externalLabel;
    private JLabel interruptLabel;
    private JButton generatorButton;
    private JButton tickButton;
    private JComboBox modeComboBox;
    private JSlider valueRVRSlider;
    private JLabel setRVRLabel;
    private JLabel modeValueLabel;
    private JTextField modeValueField;
    private JSlider valueBurstSlider;
    private JLabel setBurstLabel;
    private JRadioButton enableRadio;
    private JRadioButton sourceRadio;
    private JRadioButton isInterruptRadio;
    private JRadioButton countFlagRadio;
    private JLabel textCVRLabel;
    private JRadioButton interruptRadio;
    private JSlider valuePulseSlider;
    private JLabel setPulseLabel;
    private JTextField pulseValueField;
    private JLabel pulseValueLabel;
    private JButton resetValueButton;
    private JPanel infoPanel;
    private JPanel diodPanel;
    private JPanel textPanel;
    private Dioda interruptDiode= new Dioda();

    SysTick sysTick  = new SysTick();

    Generator generatorWindow;

    Window(Generator generator)
    {
        generatorWindow = generator;
        generatorWindow.start();
        generatorWindow.addActionListener(e -> {

            if(sysTick.isEnableFlag() && generatorWindow.getMode() == PulseSource.CONTINUOUS_MODE)
            {
                if(sysTick.getCVR() != 0)
                {
                    System.out.print("Continuous ");
                }
                sysTick.tickInternal();
            }
            else if(sysTick.isEnableFlag() && generatorWindow.getMode() == PulseSource.BURST_MODE)
            {
                if(generatorWindow.getTicksLeft() > 0)
                {
                    if(sysTick.getCVR() != 0)
                    {
                        System.out.print("Burst ");
                    }
                    sysTick.tickInternal();
                    generatorWindow.decrementTicksLeft();
                    System.out.println(generatorWindow.getTicksLeft());

                    if(generatorWindow.getTicksLeft() == 0)
                    {
                        generatorButton.doClick();
                    }
                }
            }

            cvrValueLabel.setText(String.valueOf(sysTick.getCVR()));

            if(sysTick.isInterrupt())
            {
                isInterruptRadio.setSelected(true);
            }

            if(sysTick.isCountFlag())
            {
                countFlagRadio.setSelected(true);
            }
            else
            {
                countFlagRadio.setSelected(false);
            }

            if(sysTick.getCVR() == 0 && sysTick.isInterruptFlag())
            {
                testTextArea.setText("Interrupt occurred");
            }

            if(sysTick.isCountFlag() && sysTick.isInterrupt())
            {
                interruptDiode.diodeOn();
            }
            else
            {
                interruptDiode.diodeOff();
            }
        });

        enableRadio.setEnabled(false);
        sourceRadio.setEnabled(false);
        interruptRadio.setEnabled(false);
        isInterruptRadio.setEnabled(false);
        countFlagRadio.setEnabled(false);

        enableCheckBox.addActionListener(this);
        interruptCheckBox.addActionListener(this);

        internalCheckBox.addActionListener(this);
        internalCheckBox.setSelected(true);
        internalCheckBox.setEnabled(false);
        externalCheckBox.addActionListener(this);

        modeComboBox.insertItemAt("Continuous",0);
        modeComboBox.insertItemAt("Burst",1);
        modeComboBox.setSelectedIndex(0);

        generatorButton.setFocusable(false);
        generatorButton.addActionListener(this);

        modeValueField.setEnabled(false);

        tickButton.setEnabled(false);
        tickButton.setFocusable(false);
        tickButton.addActionListener(this);

        valueBurstSlider.setMaximum(100);
        valueBurstSlider.setMinimum(0);
        valueBurstSlider.setValue(1);
        valueBurstSlider.setPaintTicks(true);
        valueBurstSlider.setMinorTickSpacing(5);
        valueBurstSlider.setPaintTrack(true);
        valueBurstSlider.setMajorTickSpacing(25);
        valueBurstSlider.setPaintLabels(true);
        valueBurstSlider.addChangeListener(this);

        valueRVRSlider.setMaximum(100);
        valueRVRSlider.setMinimum(0);
        valueRVRSlider.setValue(1);
        valueRVRSlider.setPaintTicks(true);
        valueRVRSlider.setMinorTickSpacing(5);
        valueRVRSlider.setPaintTrack(true);
        valueRVRSlider.setMajorTickSpacing(25);
        valueRVRSlider.setPaintLabels(true);
        valueRVRSlider.addChangeListener(this);

        valuePulseSlider.setMaximum(5000);
        valuePulseSlider.setMinimum(100);
        valuePulseSlider.setValue(100);
        valuePulseSlider.setPaintTicks(true);
        valuePulseSlider.setMinorTickSpacing(250);
        valuePulseSlider.setPaintTrack(true);
        valuePulseSlider.setMajorTickSpacing(1000);
        valuePulseSlider.setPaintLabels(false);
        valuePulseSlider.addChangeListener(this);

        pulseValueField.setEnabled(false);

        resetValueButton.setFocusable(false);
        resetValueButton.addActionListener(this);

        testTextArea.setEnabled(false);

        diodPanel.setMinimumSize(new Dimension(100,100));
        diodPanel.add(interruptDiode);

        frame.setContentPane(mainPanel);
        frame.setTitle("Systick Timer");
        frame.setSize(800,600);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.pack();
    }


    @Override
    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource() == enableCheckBox)
        {
            if(enableCheckBox.isSelected())
            {
                sysTick.setEnable();
                enableRadio.setSelected(true);
                testTextArea.setText("Ticking is enabled");
            }
            else if(!enableCheckBox.isSelected())
            {
                sysTick.setDisable();
                enableRadio.setSelected(false);
                testTextArea.setText("Ticking is disabled");
            }
        }

        if(e.getSource() == internalCheckBox)
        {
            if(internalCheckBox.isSelected())
            {
                tickButton.setEnabled(false);
                generatorButton.setEnabled(true);
                modeComboBox.setEnabled(true);

                internalCheckBox.setEnabled(false);
                externalCheckBox.setEnabled(true);
                externalCheckBox.setSelected(false);

                sourceRadio.setSelected(false);

                sysTick.setSourceInternal();
                testTextArea.setText("Source is internal");
            }
        }

        if(e.getSource() == externalCheckBox)
        {
            if(internalCheckBox.isSelected())
            {
                tickButton.setEnabled(true);
                generatorButton.setEnabled(false);
                modeComboBox.setEnabled(false);

                internalCheckBox.setEnabled(true);
                internalCheckBox.setSelected(false);
                externalCheckBox.setEnabled(false);

                sourceRadio.setSelected(true);

                sysTick.setSourceExternal();
                testTextArea.setText("Source is external");
            }
        }

        if(e.getSource() == interruptCheckBox)
        {
            if(interruptCheckBox.isSelected())
            {
                interruptRadio.setSelected(true);
                sysTick.setInterruptEnable();
                testTextArea.setText("Interrupt is enabled");
            }
            else if(!interruptCheckBox.isSelected())
            {
                interruptRadio.setSelected(false);
                sysTick.setInterruptDisable();
                testTextArea.setText("Interrupt is disabled");
            }
        }

        if(e.getSource() == generatorButton)
        {
            if(generatorButton.getText() == "Generator On")
            {
                if(modeComboBox.getSelectedIndex() == 0)
                {
                    generatorWindow.setMode(PulseSource.CONTINUOUS_MODE);
                }
                else if(modeComboBox.getSelectedIndex() == 1)
                {
                    generatorWindow.setMode(PulseSource.BURST_MODE);

                    if(sysTick.getCVR() == 0)
                    {
                        generatorWindow.setPulseCount(Integer.valueOf(modeValueField.getText())+1);
                    }
                    else
                    {
                        generatorWindow.setPulseCount(Integer.valueOf(modeValueField.getText()));
                    }

                    if(generatorWindow.getTicksLeft() == 0)
                    {
                        generatorWindow.resetTicksLeft();
                    }
                }

                generatorWindow.setPulseDelay(Integer.valueOf((pulseValueField.getText())));
                generatorWindow.isWorking = true;
                generatorButton.setText("Generator Off");
            }
            else if(generatorButton.getText() == "Generator Off")
            {
                generatorWindow.isWorking = false;
                generatorButton.setText("Generator On");
            }

            //Ustawienie generatora
            //System.out.println("Mode: " + generatorWindow.getMode());
            //System.out.println("PulseCount: " + generatorWindow.getPulseCount());
            //System.out.println("Ticks Left: " + generatorWindow.getTicksLeft());
        }

        if(e.getSource() == tickButton)
        {
            sysTick.tickExternal();
            cvrValueLabel.setText(String.valueOf(sysTick.getCVR()));

            if(sysTick.isInterrupt())
            {
                isInterruptRadio.setSelected(true);
            }

            if(sysTick.isCountFlag())
            {
                countFlagRadio.setSelected(true);
            }
            else
            {
                countFlagRadio.setSelected(false);
            }

            if(sysTick.getCVR() == 0 && sysTick.isInterruptFlag())
            {
                testTextArea.setText("Interrupt occurred");
            }
            else
            {
                testTextArea.setText("External Tick");
            }

            if(sysTick.isCountFlag() && sysTick.isInterrupt())
            {
                interruptDiode.diodeOn();
            }
            else
            {
                interruptDiode.diodeOff();
            }
        }

        if(e.getSource() == resetValueButton)
        {
            valueRVRSlider.setValue(valueRVRSlider.getMinimum());
            valueBurstSlider.setValue(valueBurstSlider.getMinimum());
            valuePulseSlider.setValue(valuePulseSlider.getMinimum());
        }
    }

    @Override
    public void stateChanged(ChangeEvent e)
    {
        if(e.getSource() == valueBurstSlider)
        {
            modeValueField.setText(String.valueOf(valueBurstSlider.getValue()));
            generatorWindow.setPulseCount(valueBurstSlider.getValue());
            testTextArea.setText("Burst value was set to " + valueBurstSlider.getValue());
        }

        if(e.getSource() == valueRVRSlider)
        {
            rvrValueLabel.setText(String.valueOf(valueRVRSlider.getValue()));
            //cvrValueLabel.setText(String.valueOf(valueRVRSlider.getValue()));
            sysTick.setRVR(Integer.valueOf(valueRVRSlider.getValue()));
            //sysTick.setCVR(Integer.valueOf(valueRVRSlider.getValue()));
            testTextArea.setText("RVR value was set to " + valueRVRSlider.getValue());
        }

        if(e.getSource() == valuePulseSlider)
        {
            pulseValueField.setText(String.valueOf(valuePulseSlider.getValue()));
            generatorWindow.setPulseDelay(valuePulseSlider.getValue());
            testTextArea.setText("Pulse value was set to " + valuePulseSlider.getValue() + " ms");
        }
    }
    public static void main(String[] args)
    {
        new Window(new Generator(0,1000,PulseSource.CONTINUOUS_MODE));
    }
}
