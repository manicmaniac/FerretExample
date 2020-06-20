(require '[objc :as objc])
(require '[uikit :as ui])

(let [UIResponder (objc/objc-get-class "UIResponder")
      AppDelegate (objc/objc-allocate-class-pair UIResponder "AppDelegate", 0)
      ]
  (objc/objc-register-class-pair AppDelegate)
  (objc/class-add-method AppDelegate
                         "application:didFinishLaunchingWithOptions:"
                         (objc/imp-implementation-with-ferret-lambda 2 (fn [application options]
                                                                         true))
                         "@:@@"))
(ui/application-main *command-line-args* "AppDelegate")
