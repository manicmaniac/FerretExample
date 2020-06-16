(native-declare "#import <objc/runtime.h>")
(native-declare "#undef nil")

(defn define-class [class-name superclass-name] "
    auto className = string::to<std::string>(class_name);
    auto superclassName = string::to<std::string>(superclass_name);
    auto superclass = objc_getClass(superclassName.c_str());
    auto klass = objc_allocateClassPair(superclass, className.c_str(), 0);
    objc_registerClassPair(klass);")
