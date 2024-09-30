package in.radongames.smsreceiver;

import com.radongames.core.interfaces.Converter;
import com.radongames.smslib.SmsContents;
import com.radongames.smslib.SmsTimestampConverter;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = SmsTimestampConverter.class)
public interface SmsMapper extends Converter<SmsEntity, SmsContents> {

    // Entity (Domain Object) to Bean (Data Transfer Object)
    @Override
    SmsContents forward(SmsEntity entity);

    // Bean (Data Transfer Object) to Entity (Domain Object)
    @Override
    @InheritInverseConfiguration
    SmsEntity backward(SmsContents message);
}
