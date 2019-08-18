import bean.InscricaoFederal;
import org.junit.jupiter.api.Test;

import javax.validation.Validation;
import javax.validation.Validator;

public class ConditionalTest {

    @Test
    public void chamarCBV() {
        Validator bv = Validation.buildDefaultValidatorFactory().getValidator();

        InscricaoFederal federal = new InscricaoFederal();
        federal.setTipo(InscricaoFederal.Tipo.CPF);
        federal.setNumero("321231");

        bv.validate(federal).forEach(System.out::println);
    }

}
