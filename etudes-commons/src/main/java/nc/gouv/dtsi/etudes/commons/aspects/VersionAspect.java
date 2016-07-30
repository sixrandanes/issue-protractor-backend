package nc.gouv.dtsi.etudes.commons.aspects;

import static nc.gouv.dtsi.etudes.commons.utils.Constantes.GMT11;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import nc.gouv.dtsi.etudes.commons.utils.IEntity;

/**
 * Aspect gérant la mise à jour des champs liés au versionning sur les IEntity
 *
 * @author ILDA
 */
@Aspect
@Component
public abstract class VersionAspect {

    /**
     * Mise à jour des champs de versionning (auteur et date, création ou màj)
     *
     * @param ent Entité à mettre à jour
     */
    @Before(value = "execution(* org.springframework.data.repository.CrudRepository+.save(*)) && args(ent)")
    public void onSaveEnt(final IEntity ent) {
        /*
         * Le pointcut cible la méthode save(entity) de CrudRepository en se limitant aux entités de type IEntity
         */
        onSave(ent);
    }

    /**
     * Mise à jour des champs de versionning (auteur et date, création ou màj) WARN : Dans le cas d'une collection, && args(lEnt) ne garantit pas que tous les
     * elements de la collection soient bien du type souhaité en l'occurence, IEntity ClassCastException si l'element n'est pas de type IEntity : il faut donc
     * les verifier un a un. (ou alors trouver le moyen que la méthode ne soit pas appelée avec des pointcuts dans ce cas?)
     *
     * @param lEnt Entité à mettre à jour
     */
    @Before(value = "execution(* org.springframework.data.repository.CrudRepository+.save(*)) && args(lEnt)")
    public void onSaveListEnt(final List<?> lEnt) {
        /*
         * Le pointcut cible la méthode save(listEntity) de CrudRepository en se limitant à une liste d'entités de type IEntity
         */
        lEnt.stream().forEach(ent -> onSave(ent));
    }

    private void onSave(final Object ent) {

        // Check l'instance de l'objet
        if (!(ent instanceof IEntity)) {
            return;
        }

        // Sinon, renseigner les champs auteur et date de creation/maj en fonction du cas
        // Si Id null, on est en création, sinon on est en modification.

        final IEntity entity = (IEntity) ent;
        if (entity.getId() == null) {
            // Création
            entity.setAuteurCre(getCurrentUserName());
            entity.setDateCre(LocalDateTime.now(ZoneId.of(GMT11)));
        } else {
            // Modification
            entity.setAuteurMaj(getCurrentUserName());
            entity.setDateMaj(LocalDateTime.now(ZoneId.of(GMT11)));
        }
    }

    /**
     * Récupération du nom de l'utilisateur connecté
     *
     * @return username de l'utilisateur connecté ayant fait l'action
     */
    protected abstract String getCurrentUserName();
}
