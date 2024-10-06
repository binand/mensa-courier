package in.radongames.smsreceiver;

import com.radongames.core.converters.EpochStringConverter;
import com.radongames.core.interfaces.Converter;
import com.radongames.smslib.SmsContents;

import org.mapstruct.AfterMapping;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = EpochStringConverter.class)
public interface SmsMapper extends Converter<SmsEntity, SmsContents> {

    // Entity (Domain Object) to Bean (Data Transfer Object)
    @Override
    SmsContents forward(SmsEntity entity);

    // Bean (Data Transfer Object) to Entity (Domain Object)
    @Override
    @InheritInverseConfiguration
    SmsEntity backward(SmsContents message);

    @AfterMapping
    public default SmsContents forwardId(SmsEntity entity, @MappingTarget SmsContents message) {

        /*
         * Just make sure that the DTO's ID is set to the entity's ID (retrieved from DB).
         * This is not needed; this method is here only for completeness.
         */
        message.setId(entity.getId());
        return message;
    }

    @AfterMapping
    public default SmsEntity backwardId(SmsContents message, @MappingTarget SmsEntity entity) {

        /*
         * The entity's ID should be null so that the DB generates one as part of the autoGenerate
         * process.
         */
        Long id = message.getId();
        if (id.equals(SmsContents.INVALID_ID)) {

            entity.setId(null);
        }
        return entity;
    }
}
