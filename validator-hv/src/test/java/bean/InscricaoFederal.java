package bean;

import com.github.microtweak.validator.conditional.core.ConditionalValidate;
import com.github.microtweak.validator.conditional.hv.constraint.CNPJWhen;
import com.github.microtweak.validator.conditional.hv.constraint.CPFWhen;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@ConditionalValidate
public class InscricaoFederal {

    public enum Tipo {
        CPF, CNPJ
    }

    @Setter
    @NotNull
    private Tipo tipo;

    @Setter
    @CPFWhen(expression = "this.isCpf()")
    @CNPJWhen(expression = "this.isCnpj()")
    private String numero;

    public boolean isCpf() {
        return Tipo.CPF.equals(tipo);
    }

    public boolean isCnpj() {
        return Tipo.CNPJ.equals(tipo);
    }

}

