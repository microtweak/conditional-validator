package bean;

import com.github.microtweak.validator.conditional.core.ConditionalValidate;
import com.github.microtweak.validator.conditional.hv.constraint.br.CNPJWhen;
import com.github.microtweak.validator.conditional.hv.constraint.br.CPFWhen;
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
    @CPFWhen(expression = "self.isCpf()")
    @CNPJWhen(expression = "self.isCnpj()")
    private String numero;

    public boolean isCpf() {
        return Tipo.CPF.equals(tipo);
    }

    public boolean isCnpj() {
        return Tipo.CNPJ.equals(tipo);
    }

}

