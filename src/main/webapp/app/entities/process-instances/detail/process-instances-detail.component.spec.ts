import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ProcessInstancesDetailComponent } from './process-instances-detail.component';

describe('ProcessInstances Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProcessInstancesDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: ProcessInstancesDetailComponent,
              resolve: { processInstances: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding()
        ),
      ],
    })
      .overrideTemplate(ProcessInstancesDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load processInstances on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ProcessInstancesDetailComponent);

      // THEN
      expect(instance.processInstances).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
