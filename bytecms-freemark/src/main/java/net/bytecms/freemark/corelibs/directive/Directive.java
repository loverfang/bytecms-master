package net.bytecms.freemark.corelibs.directive;

import net.bytecms.core.constants.DirectiveNameEnum;
import net.bytecms.freemark.corelibs.handler.RenderHandler;

import java.io.IOException;

/**
 * 
 * BaseDirective 指令接口
 *
 */
public interface Directive {
    /**
     * @return name
     */
     DirectiveNameEnum getDirectiveName();

    /**
     * @param handler
     * @throws IOException
     * @throws Exception
     */
     void execute(RenderHandler handler) throws IOException, Exception;
}
