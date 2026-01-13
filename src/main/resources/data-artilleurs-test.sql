INSERT INTO faq_item(question, answer, created_at, updated_at)
VALUES ('À partir de quel âge puis-je prendre une licence ?',
        'La prise de licence est possible à partir de 8 ans. Les catégories sont définies en fonction de l’âge et permettent à chacun de pratiquer dans un cadre adapté à son niveau et à sa sécurité. Pour les mineurs, une autorisation parentale est obligatoire lors de l’inscription.',
        UTC_TIMESTAMP(),
        UTC_TIMESTAMP()),
       ('Combien coûte la licence ?',
        'Le tarif de la licence varie selon la catégorie d’âge et le niveau de pratique. Il inclut l’adhésion au club, l’assurance sportive ainsi que l’accès aux entraînements. Les tarifs détaillés sont disponibles sur la page d’inscription ou directement auprès du club.',
        UTC_TIMESTAMP(),
        UTC_TIMESTAMP()),
       ('De quels équipements ai-je besoin ?',
        'Une tenue de sport adaptée est suffisante pour débuter (chaussures de sport, vêtements confortables). Certains équipements spécifiques peuvent être demandés selon la discipline et le niveau de pratique. Le club pourra vous conseiller avant l’achat de matériel plus technique.',
        UTC_TIMESTAMP(),
        UTC_TIMESTAMP()),
       ('Y a-t-il une date limite pour s’inscrire ?',
        'Les inscriptions sont généralement ouvertes en début de saison, mais restent possibles tout au long de l’année dans la limite des places disponibles. Il est toutefois recommandé de s’inscrire le plus tôt possible afin de garantir une place dans le groupe souhaité.',
        UTC_TIMESTAMP(),
        UTC_TIMESTAMP()),
       ('Quel type de gabarit est requis pour intégrer l’équipe ?',
        'Aucun gabarit particulier n’est exigé. Le club accueille des profils variés et adapte l’encadrement en fonction des capacités et des objectifs de chacun. La motivation, l’envie de progresser et l’esprit d’équipe sont les critères les plus importants.',
        UTC_TIMESTAMP(),
        UTC_TIMESTAMP()),
       ('Où se trouve le stade ?',
        'Le stade se situe à proximité du centre-ville et est facilement accessible en voiture ou en transports en commun. L’adresse exacte, ainsi que les informations d’accès et de stationnement, sont disponibles sur le site officiel du club.',
        UTC_TIMESTAMP(),
        UTC_TIMESTAMP());

INSERT INTO info_block(slot, content, created_at, updated_at)
VALUES -- Slot 2
       (2,
        'Reprise des entraînements pour la saison 2023-2024 dès **le lundi 14 août** ! Pensez à vérifier vos horaires et à préparer votre équipement.',
        UTC_TIMESTAMP(),
        UTC_TIMESTAMP()),

-- Slot 1
       (1,
        'Les inscriptions sont **ouvertes** pour la nouvelle saison. _Les places sont limitées_, n’attendez pas le dernier moment pour vous inscrire.',
        UTC_TIMESTAMP(),
        UTC_TIMESTAMP()),

-- Slot 3
       (3,
        '**Assemblée générale du club** prévue le _vendredi 22 septembre à 18h30_. La présence des licenciés est fortement recommandée.',
        UTC_TIMESTAMP(),
        UTC_TIMESTAMP()),

-- Slot 4
       (4,
        'Rappel : le port des **équipements de sécurité** est obligatoire lors de tous les entraînements et compétitions.',
        UTC_TIMESTAMP(),
        UTC_TIMESTAMP()),

-- En réserve (non affiché)
       (NULL,
        'Stage de perfectionnement organisé pendant les **vacances de la Toussaint**. Plus d’informations à venir prochainement.',
        UTC_TIMESTAMP(),
        UTC_TIMESTAMP()),

-- En réserve (non affiché)
       (NULL,
        'Félicitations à nos équipes pour leurs **excellents résultats** lors du dernier tournoi régional 👏',
        UTC_TIMESTAMP(),
        UTC_TIMESTAMP());