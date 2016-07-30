package nc.gouv.dtsi.etudes.commons.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * Classe utilitaire pour la manipulation du JSON pour les tests
 *
 * <p>
 * UNIQUEMENT POUR LES TESTS
 * </p>
 *
 * @author FFAIVE
 */
public class JsonParser {

    private static Logger LOGGER = LoggerFactory.getLogger(JsonParser.class);

    private static final DateTimeFormatter FORMAT_DATE = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter FORMAT_DATE_TIME = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    private static final DateTimeFormatter FORMAT_TIME = DateTimeFormatter.ofPattern("HH:mm:ss");

    /**
     * Cette méthode permet parser en JSON un objet, on peut ainsi le mettre directement dans le content pour simuler le body
     *
     * @param obj Objet à sérialiser en JSON
     * @return la représentation JSON de l'objet
     * @throws JsonProcessingException en cas d'erreur
     */
    public static String asJsonString(final Object obj) throws JsonProcessingException {
        try {
            final ObjectMapper mapper = new ObjectMapper();

            // En HAL+JSON, on ne veut que les champs de l'entité, et pas les
            // dépendances.
            /*
             * Transformer l'objet en Map - Remplacer les entités filles par "nomEntité": {} pour que ce soit facile à remplacer par regex - Remplacer les
             * listes d'entités par "nomChamp": [{}, {}] pour que ce soit facile à remplacer par regex
             */
            final Map<String, Object> objectMap = getMap(obj);

            // Pour éviter de planter lors de la sérialisation d'une liste vide
            // :
            mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

            return mapper.writeValueAsString(objectMap);

        } catch (final JsonProcessingException e) {
            LOGGER.error("Problème lors du parsing de l'objet", e);
            throw e;
        }
    }

    /**
     * Convertit un objet en Map. Keys : le nom des champs Values : la valeur des champs
     *
     * @param obj Objet à transformer
     * @return Une Map contenant les champs de l'objet
     */
    private static Map<String, Object> getMap(final Object obj) {
        final Map<String, Object> result = new HashMap<>();

        // Récupérer les champs de la classe et de sa classe mère (IEntity,
        // bien souvent)
        final List<Field> fields = new ArrayList<>(32);
        fields.addAll(Arrays.asList(obj.getClass().getDeclaredFields()));
        Class<?> parent = obj.getClass().getSuperclass();
        while (parent != null) {
            fields.addAll(Arrays.asList(parent.getDeclaredFields()));
            parent = parent.getSuperclass();
        }

        /*
         * Prendre tous les champs de l'entité Si c'est une autre entité, la remplacer par un nouvelle Object => {} Si c'est une liste, la remplacer par une
         * liste de 2 Object => [{}, {}] Si c'est une date, la formatter Si c'est un type primitif ou wrapper, garder sa valeur
         */
        try {
            for (final Field field : fields) {
                if (!Modifier.isStatic(field.getModifiers())) {
                    field.setAccessible(true);
                    if (field.getType().isPrimitive() || isWrapperType(field.getType())) {
                        result.put(field.getName(), field.get(obj));
                    } else if (isDateType(field.getType())) {
                        result.put(field.getName(), format(field.get(obj)));
                    } else if (field.get(obj) instanceof IEntity) {
                        result.put(field.getName(), new Object());
                    } else if (field.get(obj) instanceof List) {
                        result.put(field.getName(), Arrays.asList(new Object(), new Object()));
                    } else if (field.get(obj) instanceof Set) {
                        result.put(field.getName(), Arrays.asList(new Object(), new Object()));
                    } else if (field.get(obj) instanceof Enum) {
                        result.put(field.getName(), String.valueOf(field.get(obj)));
                    }
                }
            }
        } catch (IllegalAccessException | IllegalArgumentException e) {
            LOGGER.error("Error", e);
        }
        return result;
    }

    /**
     * Formatte les dates
     *
     * @param aObject Un objet Date à formatter
     * @return la date à son bon format
     */
    private static String format(final Object aObject) {
        if (aObject == null) {
            return "";
        } else if (aObject instanceof LocalDate) {
            return ((LocalDate) aObject).format(FORMAT_DATE);
        } else if (aObject instanceof LocalDateTime) {
            return ((LocalDateTime) aObject).format(FORMAT_DATE_TIME);
        } else if (aObject instanceof LocalTime) {
            return ((LocalTime) aObject).format(FORMAT_TIME);
        }
        return aObject.toString();
    }

    private static final Set<Class<?>> DATE_TYPES = getDateTypes();

    /**
     * Dit si une classe est une classe de Date
     *
     * @param clazz Classe à tester
     * @return true si la classe est une Date, false sinon
     */
    private static boolean isDateType(final Class<?> clazz) {
        return DATE_TYPES.contains(clazz);
    }

    /**
     * Retourne l'ensemble des classes dites Wrapper
     *
     * @return ensemble des classes dites Wrapper
     */
    private static Set<Class<?>> getDateTypes() {
        final Set<Class<?>> ret = new HashSet<>();
        ret.add(LocalDate.class);
        ret.add(LocalDateTime.class);
        ret.add(LocalTime.class);
        return ret;
    }

    private static final Set<Class<?>> WRAPPER_TYPES = getWrapperTypes();

    /**
     * Dit si une classe est une classe de Wrapper
     *
     * @param clazz Classe à tester
     * @return true si la classe est un Wrapper, false sinon
     */
    private static boolean isWrapperType(final Class<?> clazz) {
        return WRAPPER_TYPES.contains(clazz);
    }

    /**
     * Retourne l'ensemble des classes dites Wrapper
     *
     * @return ensemble des classes dites Wrapper
     */
    private static Set<Class<?>> getWrapperTypes() {
        final Set<Class<?>> ret = new HashSet<>();
        ret.add(Boolean.class);
        ret.add(Character.class);
        ret.add(Byte.class);
        ret.add(Short.class);
        ret.add(Integer.class);
        ret.add(Long.class);
        ret.add(Float.class);
        ret.add(Double.class);
        ret.add(Void.class);
        ret.add(String.class);
        ret.add(BigDecimal.class);
        return ret;
    }
}
