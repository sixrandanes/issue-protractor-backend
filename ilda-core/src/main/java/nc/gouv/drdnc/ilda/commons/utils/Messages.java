package nc.gouv.drdnc.ilda.commons.utils;

/**
 * Liste de tous les messages d'erreur utilisés dans les entitées JPA
 *
 * @author ILDA
 */
public interface Messages {

    /** Champ obligatoire */
    String MANDATORY = "Champ obligatoire";

    /** MaxLength : 3 caractères */
    String LENGTH_3 = "Ce champ doit contenir exactement 3 caractères";

    /** MaxLength : 3 caractères */
    String MAX_LENGTH_3 = "Champ limité à 3 caractères";

    /** MaxLength : 5 caractères */
    String MAX_LENGTH_5 = "Champ limité à 5 caractères";

    /** MaxLength : 50 caractères */
    String MAX_LENGTH_50 = "Champ limité à 50 caractères";

    /** MaxLength : 500 caractères */
    String MAX_LENGTH_500 = "Champ limité à 500 caractères";

	/** Size : 2 caractères */
	String SIZE_FIELD_2 = "Champ de 2 caractères";

    /** Size : 3 caractères */
    String SIZE_FIELD_3 = "Champ de 3 caractères";

}
