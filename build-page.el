(defun cam/build-page (page-name &optional header-file body-file)
  "Builds a webpage using https://github.com/vibe-876/blog-generator.
This should not be used for anything else, or by anyone else, since it
relies on for specific non-public files I've written."
  (interactive)
  (let ((header (if (equal header-file nil) "header.html" header-file))
	(body (if (equal body-file nil) "body.html" body-file)))

    (shell-command (concat "java -jar bg.jar "
			   page-name " "
			   page-name ".html "
			   header " " body))

    (message "Done :3 .")))
