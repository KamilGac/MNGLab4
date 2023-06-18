package edu.wat.tim.lab1.service;


import lombok.extern.slf4j.Slf4j;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.PolyglotException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import edu.wat.tim.lab1.repository.ProduktEntityRepository;
import edu.wat.tim.lab1.repository.KlientEntityRepository;

@Service
@Slf4j
public class ScriptService {
    private final ProduktEntityRepository ProduktEntityRepository;
    private final KlientEntityRepository KlientEntityRepository;

    @Autowired
    public ScriptService(ProduktEntityRepository ProduktEntityRepository, KlientEntityRepository KlientEntityRepository) {
        this.ProduktEntityRepository = ProduktEntityRepository;
        this.KlientEntityRepository = KlientEntityRepository;
    }

    public String exec(String script) {
        try (Context context = Context.newBuilder("js")
                .allowAllAccess(true)
                .build()) {
            var bindings = context.getBindings("js");
            bindings.putMember("ProduktEntityRepository", ProduktEntityRepository);
            bindings.putMember("KlientEntityRepository", KlientEntityRepository);
            return context.eval("js", script).toString();
        } catch (PolyglotException e) {
            log.error("Error executing", e);
            return e.getMessage() + "\n" + e.getSourceLocation().toString();
        }
    }
}