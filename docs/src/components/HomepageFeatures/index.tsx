import type {ReactNode} from 'react';
import clsx from 'clsx';
import Heading from '@theme/Heading';
import styles from './styles.module.css';

type FeatureItem = {
  title: string;
  Img: React.ComponentType<React.ComponentProps<'img'>>;
  description: ReactNode;
};

const FeatureList: FeatureItem[] = [
  {
    title: 'Easy to Use',
    Img: require('@site/static/img/presentation.png').default,
    description: (
      <>
        Demo in the hosted environment (MDB Employees) or download and run locally. Pick the approach that works best for you.
      </>
    ),
  },
  {
    title: 'Resonates with Customers',
    Img: require('@site/static/img/coding.png').default,
    description: (
      <>
        Mongo Logistics goes beyond the simple app to leverage enterprise
          frameworks to show the power of MDB with tools our customers use.
      </>
    ),
  },
  {
    title: 'Transparent',
    Img: require('@site/static/img/clear_communication.png').default,
    description: (
      <>
        Full source code is shared so that customers can view for themselves what's happening. Provides confidence that
          the comparison is fair, and gives cusotmers the ability to use the content to educate themselves.
      </>
    ),
  },
];

function Feature({title, Img, description}: FeatureItem) {
  return (
    <div className={clsx('col col--4')}>
      <div className="text--center">
        <img src={Img} className={styles.featureImg} role="img" />
      </div>
      <div className="text--center padding-horiz--md">
        <Heading as="h3">{title}</Heading>
        <p>{description}</p>
      </div>
    </div>
  );
}

export default function HomepageFeatures(): ReactNode {
  return (
    <section className={styles.features}>
      <div className="container">
        <div className="row">
          {FeatureList.map((props, idx) => (
            <Feature key={idx} {...props} />
          ))}
        </div>
      </div>
    </section>
  );
}
