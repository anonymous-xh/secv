// CheckStyle: start generated
package com.oracle.truffle.secureL;

import com.oracle.truffle.api.TruffleLanguage;
import com.oracle.truffle.api.TruffleFile.FileTypeDetector;
import com.oracle.truffle.api.TruffleLanguage.Provider;
import com.oracle.truffle.api.TruffleLanguage.Registration;
import com.oracle.truffle.api.dsl.GeneratedBy;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@GeneratedBy(SecureLLanguage.class)
@Registration(id = "secL", name = "Secure Language")
public final class SecureLLanguageProvider implements Provider {

    @Override
    public TruffleLanguage<?> create() {
        return new SecureLLanguage();
    }

    @Override
    public List<FileTypeDetector> createFileTypeDetectors() {
        return Collections.emptyList();
    }

    @Override
    public String getLanguageClassName() {
        return "com.oracle.truffle.secureL.SecureLLanguage";
    }

    @Override
    public Collection<String> getServicesClassNames() {
        return Collections.emptySet();
    }

}
