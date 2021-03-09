package net.bytecms.core.model;
import net.bytecms.core.annotation.LicenseMark;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class LicenseProperties {

    @LicenseMark
    private String version;

    @LicenseMark
    private String  organization;

    @LicenseMark
    private String domain;

    @LicenseMark
    private String startStopTime;

    @LicenseMark
    private String authorizeDesc ;

    @LicenseMark
    private String copyrightOwner;

    private String signaturer;

    private String status;
}
