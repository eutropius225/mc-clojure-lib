(in-ns 'eutros.clojurelib.lib.core)

(import net.minecraftforge.fml.loading.FMLEnvironment
        cpw.mods.modlauncher.api.INameMappingService$Domain
        net.minecraftforge.fml.common.ObfuscationReflectionHelper)

(def IS_FORGE true)
(def IS_FABRIC false)
(def IS_CLIENT (.isClient FMLEnvironment/dist))
(def MAPPINGS (keyword "obf" FMLEnvironment/naming))

(.debug LOGGER
        (str "Loading on FORGE\n"
             (if IS_CLIENT "CLIENT" "DEDICATED SERVER") " distribution\n"
             (.toUpperCase (name MAPPINGS)) " mappings"))

(defn mapped [form]
  (let [srg-name (get (meta form)
                      :obf/srg
                      form)]
    (symbol (if (= MAPPINGS :obf/srg)
              srg-name
              (ObfuscationReflectionHelper/remapName
                (case (second (str srg-name))
                  \u INameMappingService$Domain/METHOD      ;; func
                  \i INameMappingService$Domain/FIELD       ;; field
                  INameMappingService$Domain/CLASS)
                (str srg-name))))))
