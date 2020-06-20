(require '[objc :as objc])
(require '[uikit :as ui])

(let [UIResponder (objc/objc-get-class "UIResponder")
      AppDelegate (objc/objc-allocate-class-pair UIResponder "AppDelegate", 0)]
  (objc/objc-register-class-pair AppDelegate)
  (objc/class-add-method AppDelegate
                         "application:didFinishLaunchingWithOptions:"
                         (objc/imp-implementation-with-ferret-lambda
                           2
                           (fn [self application options]
                             (let [UIWindow (objc/objc-get-class "UIWindow")
                                   window (objc/objc-msg-send (objc/objc-msg-send UIWindow "alloc") "init")]
                               (let [UIViewController (objc/objc-get-class "UIViewController")
                                     viewController (objc/objc-msg-send (objc/objc-msg-send UIViewController "alloc") "init")
                                     view (objc/objc-msg-send viewController "view")]
                                 (objc/objc-msg-send window "setRootViewController:" viewController)
                                 (let [UIColor (objc/objc-get-class "UIColor")
                                       green (objc/objc-msg-send UIColor "greenColor")]
                                   (objc/objc-msg-send view "setBackgroundColor:" green)))
                               (objc/objc-msg-send window "makeKeyAndVisible"))
                             true))
                         "@:@@"))
(ui/application-main *command-line-args* "AppDelegate")
