package dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TranslatableString {
    private String es;
    private String cat;

    public String get(String language){
        if(language==null)
            return this.cat;
        else if(language.equals("es"))
            return this.es;
        else
            return this.cat;
    }

    public TranslatableString(){}

    public TranslatableString(String es, String cat){
        this.es = es;
        this.cat = cat;
    }
}
