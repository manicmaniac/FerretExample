(native-declare "#import <UIKit/UIKit.h>")
(native-declare "#import \"AppDelegate.h\"")
(native-declare "#undef nil")

(defn uiapplication-main [args, delegate-class-name]
    "std_vector _args = sequence::to<std_vector>(args);
     int status = ::UIApplicationMain((int)_args.size(), reinterpret_cast<char **>(_args.data()), nullptr, @(string::c_str(delegate_class_name)));
     exit(status);")

(uiapplication-main *command-line-args* "AppDelegate")
