package javaschool.epromo;

import java.io.Serializable;
import java.util.List;

public class Message implements Serializable {

    private String tariffName;
    private List<String> tariffOptions;


    public Message() {
    }


    public Message(String tariffName, List<String> tariffOptions) {
        this.tariffName = tariffName;
        this.tariffOptions = tariffOptions;
    }

    @Override
    public String toString() {
        return "Message{" +
                "tariffName='" + tariffName + '\'' +
                ", tariffOptions=" + tariffOptions +
                '}';
    }

    public String getTariffName() {
        return tariffName;
    }

    public void setTariffName(String tariffName) {
        this.tariffName = tariffName;
    }

    public List<String> getTariffOptions() {
        return tariffOptions;
    }

    public void setTariffOptions(List<String> tariffOptions) {
        this.tariffOptions = tariffOptions;
    }

}