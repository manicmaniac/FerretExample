(native-declare "#import <UIKit/UIKit.h>")
(native-declare "#import \"AppDelegate.h\"")
(native-declare "#undef nil")
(native-declare "#import <functional>")

(defn application-main [args delegate-class-name] "
    std::vector<char *> char_pointers{};
    for (auto& arg : sequence::to<std_vector>(args)) {
        char_pointers.push_back(string::c_str(arg));
    }
    int argc = (int)rt::count(args);
    char **argv = reinterpret_cast<char **>(char_pointers.data());
    NSString *delegate = @(string::c_str(delegate_class_name));
    int status = ::UIApplicationMain(argc, argv, nullptr, delegate);
    __result = obj<number>(status);")
