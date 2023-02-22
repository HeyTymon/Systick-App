public class SysTick implements Cortex_M0_SysTick_Interface
{
    Register csrRegister = new Register();
    Register rvrRegister = new Register();
    Register cvrRegister = new Register();

    @Override
    public void setRVR(int value)
    {
        if(value > (int)Math.pow(2,24))
        {
            rvrRegister.setRegister(value-(int)Math.pow(2,24));
        }
        else if(value < 0)
        {
            rvrRegister.setRegister((int)Math.pow(2,24) + value);
        }
        else
        {
            rvrRegister.setRegister(value);
        }
    }

    @Override
    public void setCVR(int value)
    {
        cvrRegister.setRegister(0);
        csrRegister.setBitValue(16,false);
    }

    @Override
    public void setCSR(int value)
    {
        csrRegister.setRegister(value);
    }

    @Override
    public int getCVR()
    {
        return cvrRegister.getRegister();
    }

    @Override
    public int getRVR()
    {
        return rvrRegister.getRegister();
    }

    @Override
    public int getCSR()
    {
        int value = csrRegister.getRegister(); //16
        csrRegister.setBitValue(16,false);
        return value;
    }

    @Override
    public void reset(int value)
    {
        setRVR(0);
        setCVR(0);
        setCSR(0);
    }

    @Override
    public void setEnable()
    {
        csrRegister.setRegister(csrRegister.getRegister() | 0x1);
    }

    @Override
    public void setDisable()
    {
        csrRegister.setRegister(csrRegister.getRegister() & 0xFFFFFFFE);
    }

    @Override
    public void setSourceExternal()
    {
        csrRegister.setRegister(csrRegister.getRegister() | 0x4);
    }

    @Override
    public void setSourceInternal()
    {
        csrRegister.setRegister(csrRegister.getRegister() & 0xFFFFFFFB);
    }

    @Override
    public void setInterruptEnable()
    {
        csrRegister.setRegister(csrRegister.getRegister() | 0x2);
    }

    @Override
    public void setInterruptDisable() {
        csrRegister.setRegister(csrRegister.getRegister() & 0xFFFFFFFD);
    }

    @Override
    public boolean getEnabled()
    {
        csrRegister.setBitValue(16,false);
        return csrRegister.getBitValue(0);
    }

    @Override
    public boolean getInterrupt()
    {
        csrRegister.setBitValue(16,false);
        return csrRegister.getBitValue(1);
    }

    @Override
    public boolean getSource()
    {
        csrRegister.setBitValue(16,false);
        return csrRegister.getBitValue(2);
    }

    @Override
    public boolean getCountFlag()
    {
        boolean value = csrRegister.getBitValue(16);
        csrRegister.setBitValue(16,false);
        return value;
    }

    public void tick()
    {
        if(rvrRegister.getRegister() == 0)
        {
            setDisable();
            cvrRegister.value = rvrRegister.value;
        }

        if(csrRegister.getBitValue(0))
        {
            if(cvrRegister.getRegister() > 0)
            {
                if(csrRegister.getBitValue(16))
                {
                    csrRegister.setBitValue(16,false);
                }

                cvrRegister.value--;
                System.out.println("Tick");

                if(cvrRegister.value == 0)
                {
                    csrRegister.setBitValue(16,true);
                    if(isInterruptFlag())
                    {
                        csrRegister.isInterrupt = true;
                    }
                }
            }
            else //CVR = 0
            {
                cvrRegister.value = rvrRegister.value;
            }
        }
    }

    @Override
    public void tickInternal()
    {
        //System.out.println("internal");
        if(!isSource())
        {
            tick();
        }
    }

    @Override
    public void tickExternal()
    {
        //System.out.println("external");
        if(isSource())
        {
            tick();
        }
    }

    @Override
    public boolean isCountFlag()
    {
        return csrRegister.getBitValue(16);
    }

    @Override
    public boolean isEnableFlag()
    {
        return csrRegister.getBitValue(0);
    }

    @Override
    public boolean isInterruptFlag()
    {
        return csrRegister.getBitValue(1);
    }

    @Override
    public boolean isInterrupt()
    {
        return csrRegister.getIsInterrupt();
    }

    public boolean isSource()
    {
        return csrRegister.getBitValue(2);
    }


}