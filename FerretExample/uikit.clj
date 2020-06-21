(native-declare "#define nil NULL")
(native-declare "#import <UIKit/UIKit.h>")
(native-declare "#undef nil")

(defn uiapplication-main [args delegate-class-name] "
    std::vector<const char *> char_pointers{};
    for (auto& arg : sequence::to<std_vector>(args)) {
        const char *s = string::to<std::string>(arg).c_str();
        char_pointers.push_back(s);
    }
    int argc = (int)rt::count(args);
    const char **argv = reinterpret_cast<const char **>(char_pointers.data());
    NSString *delegate = @(string::to<std::string>(delegate_class_name).c_str());
    int status = ::UIApplicationMain(argc, (char **)argv, nullptr, delegate);
    return obj<number>(status);")

(def *flexible-width-and-height*
  "return obj<value<UIViewAutoresizing>>(UIViewAutoresizingFlexibleWidth | UIViewAutoresizingFlexibleHeight);")

(def *scale-aspect-fit*
  "return obj<value<UIViewContentMode>(UIViewContentModeScaleAspectFit);")
