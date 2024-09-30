package in.radongames.smsreceiver;

import com.radongames.core.interfaces.Converter;
import com.radongames.smslib.SmsContents;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = SmsMapper.class)
public interface SmsListMapper extends Converter<List<SmsEntity>, List<SmsContents>> {

    // Entity (Domain Object) to Bean (Data Transfer Object)
    @Override
    List<SmsContents> forward(List<SmsEntity> entities);

    // Bean (Data Transfer Object) to Entity (Domain Object)
    @Override
    @InheritInverseConfiguration
    List<SmsEntity> backward(List<SmsContents> messages);
}
